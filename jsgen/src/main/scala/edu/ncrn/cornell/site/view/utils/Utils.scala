package edu.ncrn.cornell.site.view.utils

import mhtml._
import monix.execution.Scheduler.Implicits.global

import scala.collection.breakOut
import scala.concurrent.Future
import scala.util.Try
import scala.xml.{Group, Node}

object Utils {

  implicit class IterableMhtml[T, C[X] <: Iterable[X]](val list: C[T]) extends AnyVal {
    def mapToNode(fn: T => Node): Node =
      if (list.size > 1)
        Group( list.map(item => fn(item))(breakOut) )
      else list.headOption match {
        case Some(tv) => fn(tv)
        case None => <div></div>
      }
  }

  implicit class RxNode(val rxNode: Rx[Node]) extends AnyVal {
    def toNode(errNode: Node = <div>Error/404</div>): Node = {
      val nodeOuter = <div>{ rxNode }</div>
      nodeOuter.child.headOption match {
        case Some(nd) =>  <div class="debug">{ nd }</div>
        case None => errNode
      }
    }
  }


  //TODO: use version now in in mhtml:
  def fromFuture[T](future: Future[T]): Rx[Option[Try[T]]] = {
    val result = Var(Option.empty[Try[T]])
    future.onComplete(x => result := Some(x))
    result
  }

  def stripUriHash(uriString: String): String = {
    val hashIndex = uriString.indexOf("#")
    val endPos: Int = if (hashIndex > 0) hashIndex else uriString.length
    uriString.substring(0, endPos)
  }

}