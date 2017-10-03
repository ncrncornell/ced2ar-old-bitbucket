package edu.ncrn.cornell.site.view.component

import edu.ncrn.cornell.site.view.routing.{EndPoints, HostConfig, Router}
import edu.ncrn.cornell.site.view.utils.Utils._

import scala.xml.{Group, Node, Text}
import mhtml._
import org.scalajs.dom

import scala.collection.breakOut
import scala.util.{Failure, Success}
import fr.hmil.roshttp.HttpRequest
import fr.hmil.roshttp.response.SimpleHttpResponse
import monix.execution.Scheduler.Implicits.global
import io.circe._
import io.circe.parser._

object CodebookList {

  type CodebookNames = Map[String, String]

  val model: Rx[CodebookNames] = {
    val request: Rx[HttpRequest] = EndPoints.codebook.map(ep =>
      HttpRequest(ep).withHeader("Content-Type", "application/javascript")
    )

    val codebookNames: Rx[CodebookNames] = request.flatMap(req =>
      fromFuture(req.send()).map {
        case Some(resTry) => resTry match {
          case res: Success[SimpleHttpResponse] =>
            decode[CodebookNames](res.get.body) match {
              case Left(detailFailure) =>
                println("Error decoding codebook list: " + detailFailure.toString)
                Map.empty
              case Right(newDetails) => newDetails
            }
          case err: Failure[SimpleHttpResponse] =>
            println("Error retrieving codebook list: " + err.toString)
            Map.empty
        }
        case None => Map.empty
      }
    )

    codebookNames
  }


  def view(codebookNames: Rx[CodebookNames]): Node = {
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

    Component[CodebookNames](router.view, model)
  }

}
