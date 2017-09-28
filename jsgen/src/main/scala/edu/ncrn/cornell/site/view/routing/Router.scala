package edu.ncrn.cornell.site.view.routing

import mhtml.{Rx, Var}

import scala.xml.Node
import Router._

/**
  * A recursive, component-level router. In general each component should define its own route
  * if a route is needed at all.
  */
case class Router(remainingPath: Rx[String], route: String => Node) {
  val view: Rx[Node] = remainingPath.map{rPath => route(rPath)}

}
object Router {

  val delim: String = "#/"

  //TODO: handle case where not splittable

  /**
    * Utiltiy to split route
    * @param routeIn
    * @return (current route, child route)
    */
  def splitRoute(routeIn: String): (String, String) = {
    val (fst, snd) =
      if (routeIn.contains(delim))
        routeIn.splitAt(routeIn.indexOfSlice(delim))
      else (routeIn, "")
    (fst, snd.replaceFirst(delim, ""))
  }
}
