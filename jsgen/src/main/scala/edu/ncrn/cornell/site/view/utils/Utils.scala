package edu.ncrn.cornell.site.view.utils

import mhtml._
import monix.execution.Scheduler.Implicits.global
import org.scalajs.dom.raw.{XPathNSResolver, XPathResult}

import scala.collection.breakOut
import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.annotation.{JSGlobal, JSImport}
import org.scalajs.dom.{DOMParser, Document, XMLSerializer, document, Node => DomNode}

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

  //TODO: use Kantan XPath if making extensive use of this
  def getElementByXpath(xpath: String, doc: Document): XPathResult =
    doc.evaluate(
      xpath, doc, null.asInstanceOf[XPathNSResolver],
      XPathResult.FIRST_ORDERED_NODE_TYPE, null
    )

  def htmlToXHTML(input: String)
  (implicit parser: DOMParser, serializer: XMLSerializer): String = {
    val doc = parser.parseFromString(input, "text/html")
    val body = getElementByXpath("/html/body", doc).singleNodeValue
    val bodyXmlString = serializer.serializeToString(body)
    val xmldoc = parser.parseFromString(bodyXmlString, "application/xml")
    xmldoc.firstElementChild.innerHTML
  }

  def stripUriHash(uriString: String): String = {
    val hashIndex = uriString.indexOf("#")
    val endPos: Int = if (hashIndex > 0) hashIndex else uriString.length
    uriString.substring(0, endPos)
  }

}