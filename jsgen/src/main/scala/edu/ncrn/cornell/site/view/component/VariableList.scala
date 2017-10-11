package edu.ncrn.cornell.site.view.component

import edu.ncrn.cornell.service.api._
import edu.ncrn.cornell.site.view.comm.Request.requestDecodeIterable
import edu.ncrn.cornell.site.view.routing.{EndPoints, Router}
import edu.ncrn.cornell.site.view.utils.Utils._
import fr.hmil.roshttp.HttpRequest
import mhtml._
import scala.collection.immutable.ListMap

import scala.xml.Node


object VariableList {

  def model(cidOpt:  Option[CodebookId]): Rx[VarListModel] = {
    val request: Rx[HttpRequest] = cidOpt match {
      case Some(cid) => EndPoints.variable(cid).map(ep =>
        HttpRequest(ep).withHeader("Content-Type", "application/javascript")
      )
      case None => EndPoints.variable.map(ep =>
        HttpRequest(ep).withHeader("Content-Type", "application/javascript")
      )
    }

    val varNames = requestDecodeIterable[VarNameItem, VarNameCollection](request).map{
      iter: VarNames => ListMap(iter.sortBy(_._1):_*)
    }
    varNames.map(vns => VarListModel(cidOpt, vns))
  }


  def view(vlistModel: Rx[VarListModel]): Node = {
    val varRows: Rx[Node] = vlistModel.map { vm =>
      <table class="table table-striped table-hover">
        <thead>
          <tr>
            <th>Variable Name</th>
            <th>Variable Label</th>
            <th>Codebook</th>
          </tr>
        </thead>
        <tbody>
          {vm.varNames.mapToNode { case (varName, (varLabel, cbHandle)) =>
          <tr>
            <td><a href={s"#/codebook/$cbHandle/var/$varName"}>{ varName }</a></td>
            <td>{ varLabel }</td>
            <td><a href={s"#/codebook/$cbHandle"}>{ cbHandle }</a></td>
          </tr>
          }}
        </tbody>
      </table>
    }

    <div class="container-fluid" style="margin-left: 3%">
      <h1>Variables</h1>
      { varRows }
    </div>
  }

  def apply(cidOpt: Option[CodebookId], route: Rx[String]): VariableList = {
    lazy val thisModel = model(cidOpt)
    lazy val modelView = view(thisModel)
    def thisRoute(path: Rx[String]): Node = {
      val (curPathRx, childPathRx) = Router.splitRoute(path)
      val nodeRx: Rx[Node] = curPathRx.map{curPath: String =>
        if (curPath == "") modelView
        else thisModel.map{tm => tm.varNames.get(curPath) match {
          case Some(vd) =>
            println(s"vlist router; cb=${vd._2}, vid=$curPath")
            Variable(vd._2, curPath).view()
          case None => <div>Error/404</div>
        }}.toNode()
        //TODO add check on codebook handle above?
        //else Rx(<div>Make An Error page</div>)
      }
      nodeRx.toNode()
    }
    val router = Router(route, thisRoute)

    Component.applyLazy[VarListModel](router.view, thisModel)
  }

}
