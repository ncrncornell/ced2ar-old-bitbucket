package edu.ncrn.cornell.site.view.component

import edu.ncrn.cornell.service.api._
import edu.ncrn.cornell.site.view.comm.Request.requestDecodeIterable
import edu.ncrn.cornell.site.view.routing.{EndPoints, HostConfig}
import edu.ncrn.cornell.site.view.utils.Utils

import scala.xml.{Group, Node, Text}
import mhtml._
import org.scalajs.dom
import fr.hmil.roshttp.HttpRequest


object Codebook {

  def model(handle: String): Rx[CodebookDetails] = {
    val request: Rx[HttpRequest] = EndPoints.codebook(handle).map(ep =>
      HttpRequest(ep).withHeader("Content-Type", "application/javascript")
    )

    requestDecodeIterable[CodebookItem, CodebookCollection](request)
  }

  def view(details: Rx[CodebookDetails], handle: String): Node = {
    val collapsibleFields = Set("Files")

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

  def apply(handle: String): Codebook = {
    val details = model(handle)
    TaggedComponent[CodebookDetails, String](view(details, handle), details, handle)
  }
}
