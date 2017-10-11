// This implementation is mostly copied from Binding.scala TodoMVC example:
// https://github.com/ThoughtWorksInc/todo/blob/master/js/src/main/scala/com/thoughtworks/todo/Main.scala
package edu.ncrn.cornell.site.view


import scala.xml._
import mhtml._
import cats.implicits._
import edu.ncrn.cornell.site.view.component._
import edu.ncrn.cornell.site.view.component.editor.{Editor, NeptuneStyles}
import edu.ncrn.cornell.site.view.routing.{HostConfig, Router}
import edu.ncrn.cornell.site.view.utils.Utils._
import mhtml.implicits.cats._
import org.scalajs.dom
import org.scalajs.dom.{DOMList, Event, KeyboardEvent, html}
import org.scalajs.dom.ext.KeyCode
import org.scalajs.dom.ext.LocalStorage
import org.scalajs.dom.ext.PimpedNodeList

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}


object Ced2ar {

  @JSExportTopLevel("edu.ncrn.cornell.site.view")
  protected def getInstance(): this.type = this


  object View {
    val ced2ar = Group(Seq(Text("CED"), <sup>2</sup>, Text("AR")))

    def space(nn: Int): Node = Group(Seq.fill(nn)(EntityRef("nbsp")))

    def masterDiv(content: Node): Node = <div class="container-fluid">{content}</div>

    def masterTable(content: Node): Node =
      <table cls = "table table-striped table-hover">{content}</table>
    def indentDiv(content: Node): Node =
      <div class = "container-fluid" style = "margin-left: 3%">{content}</div>

    lazy val topBanner: Node =
      <div class = "navbar" style = "background-color: #B40404;">
        <div>
          <div style = "font-family: 'Fjord One', 'Palatino Linotype', 'Book Antiqua', Palatino, serif;">
            <h1 style = "color: #FFFFFF">{space(3)}{ced2ar}</h1>
            <h5 style = "color: #FFFFFF">{space(5)}
              Development Server - The Comprehensive Extensible Data Documentation and Access Repository
            </h5>
          </div>
          <div class = "row">
            <div class = "col-sm-12"> </div>
          </div>
        </div>
      </div>

    lazy val navBar: Node = <nav class = "navbar navbar-inverse">
      <div class = "navbar-collapse">
        <ul class = "nav navbar-nav">
          <li class = "divider-vertical hidden-xs"/>
          <li class = "dropdown">
            <a href = "#" class = "dropdown-toggle" data-toggle = "dropdown">
              Browse Variables <b class = "caret"/>
            </a>
            <ul class = "dropdown-menu">
              <li><a href ="#">View All</a></li>
              <li><a href ="#">Sort Alphabetically</a></li>
              <li><a href ="#">Sort by Group</a></li>
            </ul>
          </li>
          {
            val navItems = Map[String, String](
              "Browse by Codebook" ->  "#/codebook",
              "Browse by Variable" -> "#",
              "Upload a Codebook" -> "#",
              "Documentation" -> "#",
              "About" -> "#/about"
            )
            Group(navItems.toSeq.map(nItem =>  Group(
                <li class="divider-vertical hidden-xs"/>
                <li><a href={ nItem._2 }>{ nItem._1 }</a></li>
            )))
          }
        </ul>
      </div>
    </nav>


    val testEditor = Editor.editor()

    val configView = <div>
      <p>
        {HostConfig.currentApiUri.map { curUri =>
          s"Current API URI: ${curUri.toString}"
        }}
      </p>
      {HostConfig.apiUriApp.app._1}
    </div>

    val demoView = <div>
      {configView}
      <h2>Demo editor</h2>
      {testEditor.view()}
      <h3>Begin editor output</h3>
      {testEditor.model()}
      <h3>End editor output</h3>
    </div>

    val aboutComp = About()

    private def thisRoute(path: Rx[String]): Node = {
      val (curPathRx, childPathRx) = Router.splitRoute(path)
      lazy val codebookList = CodebookList(childPathRx)
      curPathRx.map{curPath =>
        if (curPath == "about") aboutComp.view()
        else if (curPath == "codebook") codebookList.view()
        else demoView
      }.toNode()
    }
    val router = Router(Router.routePath, thisRoute)


    def index: Node = {masterDiv(
      <div>
        {topBanner}
        {navBar}
        {Router.breadCrumbs}
        {router.view}
      </div>
    )}
  }

  @JSExport
  def main(args: Array[String]): Unit = {
    println("Hello!")
    val cssUrls = Seq(
      "./target/css/bootstrap.min.css",
      "./target/css/bootstrap-theme.min.css"
    )

    //TODO: for locally optimized js, can switch based on build settings
    val bodyScriptUrls = Seq(
      "./target/js/jquery.slim.min.js",
      "./target/js/bootstrap.min.js"
    )
    val bodyScripts = Group( bodyScriptUrls.map(scriptUrl =>
      <script type="application/javascript" src={scriptUrl}></script>
    ))

    dom.document.getElementsByTagName("head").headOption match {
      case Some(head) =>
        val linkRelCss =
          Group(cssUrls.map(cssUrl => <link rel="stylesheet" href={cssUrl}/>))
        mount(head, linkRelCss)
      case None => println("WARNING: no <head> element in enclosing document!")
    }

    val appContainer = dom.document.getElementById("application-container")
    val bodyScriptContainer = dom.document.getElementById("body-scripts")

    mount(appContainer, View.index)
    mount(bodyScriptContainer, bodyScripts)

  }
}
