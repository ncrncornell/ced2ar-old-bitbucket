package edu.ncrn.cornell.site.view.component

import edu.ncrn.cornell.service.api._
import edu.ncrn.cornell.site.view.comm.Request.requestDecodeIterable
import edu.ncrn.cornell.site.view.routing.{EndPoints, Router}
import edu.ncrn.cornell.site.view.utils.Utils._
import edu.ncrn.cornell.site.view.utils.Field.renderField

import scala.xml.Node
import mhtml._
import fr.hmil.roshttp.HttpRequest


object Codebook {

  def model(cbHandle: String): Rx[CodebookDetails] = {
    val request: Rx[HttpRequest] = EndPoints.codebook(cbHandle).map(ep =>
      HttpRequest(ep).withHeader("Content-Type", "application/javascript")
    )

    requestDecodeIterable[CodebookItem, CodebookCollection](request)
  }

  def view(details: Rx[CodebookDetails], cbHandle: CodebookId): Node = {
    val collapsibleFields = Set("Files")

    <div>
      <div>
        <a href={s"#/codebook/$cbHandle/var"}>View Variables</a>
      </div>
      <div>
        {details.map(cd => cd.map {
        case (fieldName, fieldValues) =>
          renderField(fieldName, fieldValues, collapsibleFields)
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
