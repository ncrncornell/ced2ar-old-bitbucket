package edu.ncrn.cornell.site.view.routing

import mhtml.{Rx, Var}

import scala.xml.Node
import Router._
import org.scalajs.dom
import org.scalajs.dom.Event
import edu.ncrn.cornell.site.view.utils.Utils._

import scala.collection.mutable.WrappedArray


/**
  * A recursive, component-level router. In general each component should define its own route
  * if a route is needed at all.
  */
case class Router(remainingPath: Rx[String], route: Rx[String] => Node) {
  val view: Node = route(remainingPath)

}
object Router {

  val routePath: Rx[String] = Rx(dom.window.location.hash).merge{
    val updatedHash = Var(dom.window.location.hash)
    dom.window.onhashchange = (ev: Event) => {
      updatedHash := dom.window.location.hash
    }
    updatedHash.map(hash => hash.replaceFirst("#/", ""))
  }

  def pathUptoIdx(pathParts: Iterable[String], idx: Int): String =
    "#/" + pathParts.take(idx + 1).mkString("/")

  val breadCrumbs: Rx[Node] = routePath.map { routeHash =>
    val ignoreInitCrumbs = Set("", "app", "about")
    val pathSplit: WrappedArray[String] = routeHash.split('/')
    val crumbs: WrappedArray[String] = pathSplit.headOption match {
      case Some(cr) =>
        if (ignoreInitCrumbs.contains(cr)) pathSplit.drop(1)
        else pathSplit
      case None => pathSplit
    }
    val (otherCrumbs, thisCrumb) = crumbs.splitAt(crumbs.length-1)
    if (crumbs.nonEmpty) {
      <ol class="breadcrumb">
        {otherCrumbs.zipWithIndex.mapToNode { case (cr: String, idx: Int) =>
        <li class="breadcrumb-item">
          <a href={pathUptoIdx(otherCrumbs, idx)}>{ cr }</a>
        </li>
      }}{thisCrumb.mapToNode(cr => <li class="breadcrumb-item active">
        {cr}
      </li>)}
      </ol>
    }
    else <div></div>
  }


  //TODO: handle case where not splittable

  /**
    * Utiltiy to split route
    * @param routeInRx
    * @return (current route, child route)
    */
  def splitRoute(routeInRx: Rx[String]): (Rx[String], Rx[String]) = {
    val splitRx = routeInRx.map { routeIn =>
      val (fst, snd) =
        if (routeIn.contains("/"))
          routeIn.splitAt(routeIn.indexOfSlice("/"))
        else (routeIn, "")
      (fst, snd.replaceFirst("/", ""))
    }
    (splitRx.map(tup => tup._1), splitRx.map(tup => tup._2))
  }
}
