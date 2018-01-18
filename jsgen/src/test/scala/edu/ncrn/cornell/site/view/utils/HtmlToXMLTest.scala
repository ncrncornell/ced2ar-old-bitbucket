package edu.ncrn.cornell.site.view.utils

import org.scalajs.dom.{DOMParser, XMLSerializer}
import org.scalatest.FunSuite
import org.scalatest.Matchers

import Utils._

//FIXME: also see https://github.com/scala-js/scala-js/issues/2635 for possible fix regarding error for jsDom in tests: "[error] Error: Uncaught [ReferenceError: exports is not defined]"
class HtmlToXMLTest extends FunSuite with Matchers {

  val bodyXpath = "/html/body"

  implicit val parser: DOMParser = new DOMParser()
  implicit val serializer: XMLSerializer = new XMLSerializer()

  test("htmlToXHML returns simple string unchanged") {
    val inputStr = "this is a test"
    //FIXME: depends on https://github.com/tmpvar/jsdom/issues/1368
    // val outputStr = htmlToXHML(inputStr)
    // outputStr should equal (inputStr)
  }


}
