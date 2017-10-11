package edu.ncrn.cornell.site.view.component

import edu.ncrn.cornell.service.api.VarDetails
import edu.ncrn.cornell.site.view.routing.{EndPoints, HostConfig}
import edu.ncrn.cornell.site.view.utils.Utils
import fr.hmil.roshttp.HttpRequest
import fr.hmil.roshttp.response.SimpleHttpResponse
import io.circe.parser._
import mhtml._
import monix.execution.Scheduler.Implicits.global
import org.scalajs.dom

import scala.util.{Failure, Success}
import scala.xml.{Group, Node, Text}


object Variable {


  def model(handle: String): Rx[VarDetails] = {
    val request: Rx[HttpRequest] = EndPoints.variable(handle).map(ep =>
      HttpRequest(ep).withHeader("Content-Type", "application/javascript")
    )

    val details: Rx[VarDetails] = request.flatMap(req =>
      Utils.fromFuture(req.send()).map {
        case Some(resTry) => resTry match {
          case res: Success[SimpleHttpResponse] =>
            decode[VarDetails](res.get.body) match {
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

  def view(details: Rx[VarDetails], handle: String): Node = {
    val collapsibleFields = Set("Values")

    def renderField(fieldName: String, fieldValues: List[String]): Node = {

      val glyphClicked: Var[Boolean] = Var(false)
      val glyphClass: Rx[String] = glyphClicked.map {
        case false => "glyphicon-menu-right"
        case true => "glyphicon-menu-down"
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

  def apply(handle: String): Variable = {
    val details = model(handle)
    TaggedComponent.applyLazy[VarDetails, String](view(details, handle), details, handle)
  }
}
