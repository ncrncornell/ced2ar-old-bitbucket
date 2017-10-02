package edu.ncrn.cornell.site.view.component

import edu.ncrn.cornell.site.view.Utils
import edu.ncrn.cornell.site.view.routing.{EndPoints, HostConfig}

import scala.xml.{Group, Node, Text}
import mhtml._

import org.scalajs.dom


import scala.util.{Failure, Success}
import fr.hmil.roshttp.HttpRequest
import fr.hmil.roshttp.response.SimpleHttpResponse
import monix.execution.Scheduler.Implicits.global
import io.circe._
import io.circe.parser._


object Codebook {

  type CodebookDetails = List[(String, List[String])]

  def apply(handle: String): Codebook = {

    def model(handle: String): Rx[CodebookDetails] = {
      val request: Rx[HttpRequest] = EndPoints.codebook(handle).map(ep =>
        HttpRequest(ep).withHeader("Content-Type", "application/javascript")
      )

      val details: Rx[CodebookDetails] = request.flatMap(req =>
        Utils.fromFuture(req.send()).map {
          case Some(resTry) => resTry match {
            case res: Success[SimpleHttpResponse] =>
              decode[CodebookDetails](res.get.body) match {
                case Left(detailFailure) =>
                  println("Error decoding codebook details: " + detailFailure.toString)
                  Nil
                case Right(newDetails) => newDetails
              }
            case err: Failure[SimpleHttpResponse] =>
              println("Error retrieving codebook details: " + err.toString)
              Nil
          }
          case None => Nil
        }
      )
      details
    }

    def view(details: Rx[CodebookDetails], handle: String): Node = {
      val collapsibleFields = Set("Files")

      def renderField(fieldName: String, fieldValues: List[String]): Node = {

        val glyphClicked: Var[Boolean] = Var(false)
        val glyphClass: Rx[String] = glyphClicked.map {
          case false => "glyphicon-menu-down"
          case true => "glyphicon-menu-right"
        }

        if (collapsibleFields.contains(fieldName))
          <div>
            <h3>
              { glyphClass.map{ gclass =>
                <a class={s"glyphicon $gclass"}
                   href={s"#$fieldName-detail"} data-toggle="collapse"
                   onclick={ (ev: dom.Event) => { glyphClicked.update(click => !click) } }>
                  {fieldName}
                </a>
              }}
              <div id={s"$fieldName-detail"} class="collapse">
                <p>{ fieldValues.map(fv => Group(Seq(Text(fv), <br />))) }</p>
              </div>
            </h3>
          </div>
        else
          <div>
            <h3>
              {fieldName}
            </h3>
            <p>
              {fieldValues.mkString("\n")}
            </p>
          </div>
      }

      <div>
        <p>
          {HostConfig.currentApiUri.map { curUri =>
          s"Current API URI: ${curUri.toString}"
        }}
        </p>{HostConfig.apiUriApp.app._1}<ol cls="breadcrumb">
        <li>
          <a href={s"codebook"}></a>
          Codebooks</li>
        <li cls="active">
          {handle}
        </li>
      </ol>
        <div>
          <a href={s"codebook/$handle/var"}>View Variables</a>
        </div>
        <div>
          {details.map(cd => cd.map {
          case (fieldName, fieldValues) => renderField(fieldName, fieldValues)
        })}
        </div>
      </div>

    }
    val details = model(handle)

    TaggedComponent[CodebookDetails, String](view(details, handle), details, handle)
  }
}
