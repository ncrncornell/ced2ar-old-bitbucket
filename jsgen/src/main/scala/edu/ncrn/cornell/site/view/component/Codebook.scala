package edu.ncrn.cornell.site.view.component

import edu.ncrn.cornell.service.api._
import edu.ncrn.cornell.site.view.comm.Request.requestDecodeIterable
import edu.ncrn.cornell.site.view.routing.{EndPoints, Router}
import edu.ncrn.cornell.site.view.utils.Utils._

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
      <div>
        <a href={s"#/codebook/$handle/var"}>View Variables</a>
      </div>
      <div>
        {details.map(cd => cd.map {
        case (fieldName, fieldValues) => renderField(fieldName, fieldValues)
      })}
      </div>
    </div>

  }

  def apply(route: Rx[String], handle: String): Codebook = {
    val details = model(handle)
    def thisRoute(path: Rx[String]): Node = {
      val (curPathRx, childPathRx) = Router.splitRoute(path)
      lazy val varList = VariableList(Option(handle), childPathRx)
      val nodeRx: Rx[Node] = curPathRx.map{curPath: String =>
        if (curPath == "") view(details, handle)
        else varList.view()
        //TODO add check on codebook handle above?
        //else Rx(<div>Make An Error page</div>)
      }
      nodeRx.toNode()
    }
    val router = Router(route, thisRoute)

    TaggedComponent.applyLazy[CodebookDetails, String](router.view, details, handle)
  }
}
