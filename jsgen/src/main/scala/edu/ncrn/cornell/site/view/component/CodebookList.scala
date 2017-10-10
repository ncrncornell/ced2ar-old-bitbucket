package edu.ncrn.cornell.site.view.component

import edu.ncrn.cornell.service.api._
import edu.ncrn.cornell.site.view.routing.{EndPoints, HostConfig, Router}
import edu.ncrn.cornell.site.view.comm.Request.requestDecodeIterable
import edu.ncrn.cornell.site.view.utils.Utils._

import scala.xml.{Group, Node, Text}
import mhtml._

import fr.hmil.roshttp.HttpRequest


object CodebookList {

  val model: Rx[CodebookNameMap] = {
    val request: Rx[HttpRequest] = EndPoints.codebook.map(ep =>
      HttpRequest(ep).withHeader("Content-Type", "application/javascript")
    )

    requestDecodeIterable[CodebookNameItem, CodebookNameCollection](request).map{
      iter => iter.toMap
    }
  }


  def view(codebookNames: Rx[CodebookNameMap]): Node = {
    val codebookLinks: Rx[Node] =
      codebookNames.map{cbks => cbks.mapToNode{ case (cbHandle, cbName) =>
        <p>
          <a href={s"#/codebook/$cbHandle"}>
            {cbName}
          </a>
        </p>
      }}

    <div class="container-fluid" style="margin-left: 3%">
     <h1>Codebooks</h1>
     { codebookLinks }
    </div>
  }

  private def thisRoute(path: Rx[String]): Node = {
    val (curPathRx, childPathRx) = Router.splitRoute(path)
    val nodeRx: Rx[Node] = curPathRx.map{curPath: String =>
      if (curPath == "") view(model)
      else Codebook(curPath).view
      //TODO add check on codebook handle above?
      //else Rx(<div>Make An Error page</div>)
    }
    nodeRx.toNode()
  }

  def apply(route: Rx[String]): CodebookList = {
    val router = Router(route, thisRoute)

    Component[CodebookNameMap](router.view, model)
  }

}
