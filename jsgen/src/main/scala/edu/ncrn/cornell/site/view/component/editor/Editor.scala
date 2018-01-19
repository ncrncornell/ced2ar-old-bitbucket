package edu.ncrn.cornell.site.view.component.editor

import scala.scalajs.js
import org.scalajs.dom
import dom.{document, window}
import dom.raw.{MouseEvent, MutationObserver, MutationObserverInit, MutationRecord}
import edu.ncrn.cornell.site.view.component.Component
import edu.ncrn.cornell.site.view.utils.Utils._
import mhtml.{Rx, Var}
import org.scalajs.dom.html.Div
import org.scalajs.dom.raw.{DOMParser, XMLSerializer}

import scala.concurrent.{Future, Promise}
import scala.xml.Node
import scala.concurrent.ExecutionContext.Implicits.global
import scalacss.ProdDefaults._

/**

  Adapted from https://github.com/amirkarimi/neptune

  MIT license follows:

  Copyright (c) 2017 Amir Karimi
  Copyright (c) 2017 Cornell University

  */


object Editor {

  val stylesLoaded: Promise[Unit] = Promise()

  case class Action(icon: Node, title: String, command: () => Unit)

  object Act {
    def apply(icon: Node, title: String)(command: => Unit): Action = {
      Action.apply(icon, title, () => command)
    }
  }

  case class Settings(actions: Seq[Action] = actions, styleWithCss: Boolean = false)

  val actions = Seq(
    Act(<b>💾</b>, "Save") {
      () => Unit // TODO: Unimplemented
    },
    Act(<b>B</b>, "Bold") {
      exec("bold")
    },
    Act(<i>I</i>, "Italic") {
      exec("italic")
    },
    Act(<u>U</u>, "Underline") {
      exec("underline")
    },
    Act(<strike>S</strike>, "strikeThrough") {
      exec("strikeThrough")
    },
    Act(<b>H<sub>1</sub></b>, "Heading 1") {
      exec("formatBlock", "<H1>")
    },
    Act(<b>H<sub>2</sub></b>, "Heading 2") {
      exec("formatBlock", "<H2>")
    },
    Act(<div>¶</div>, "Paragraph") {
      exec("formatBlock", "<P>")
    },
    Act(<div>“”</div>, "Quote") {
      exec("formatBlock", "<BLOCKQUOTE>")
    },
    Act(<div>#</div>, "Ordered List") {
      exec("insertOrderedList")
    },
    Act(<div>•</div>, "Unordered List") {
      exec("insertUnorderedList")
    },
    Act(<div>{"</>"}</div>, "Code") {
      exec("formatBlock", "<PRE>")
    },
    Act(<div>―</div>, "Horizontal Line") {
      exec("insertHorizontalRule")
    },
    Act(<div>🔗</div>, "Link") {
      val url = window.prompt("Enter the link URL")
      if (url.nonEmpty) exec("createLink", url)
    },
    Act(<div>📷</div>, "Image") {
      val url = window.prompt("Enter the image URL")
      if (url.nonEmpty) exec("insertImage", url)
    }
  )

  private def exec(command: String) = {
    document.execCommand(command, false, null)
  }

  private def exec(command: String, value: scalajs.js.Any) = {
    document.execCommand(command, false, value)
  }


  def editor(settings: Settings = Settings(), initText: String = ""): Component[String] = {

    implicit val parser: DOMParser = new DOMParser()
    implicit val serializer: XMLSerializer = new XMLSerializer()

    stylesLoaded.isCompleted match {
      case true => ()
      case false =>
        NeptuneStyles.addToDocument()
        stylesLoaded.completeWith(Future(()))
    }

    // TODO: settings.classes.actionbar
    val actionBar = <div class={NeptuneStyles.neptuneActionbar.htmlClass}>{
      actions.map { action =>
        <button class={ NeptuneStyles.neptuneButton.htmlClass }
                title={ action.title }
                onclick={ (ev: MouseEvent) => action.command() }
        >{ action.icon }</button>
      }
    }</div>

    val content: Var[String] = Var(htmlToXHTML(initText))

    def updateContent(domNode: Div): Unit = {
      domNode.innerHTML = initText
      def observerCallback(muts: js.Array[MutationRecord], obs: MutationObserver) = {
        content := htmlToXHTML(domNode.innerHTML)
      }
      val contentObserver: MutationObserver = new MutationObserver(observerCallback _)
      val contentObserverParams = new js.Object{
        val subtree = true
        val attributes = true
        val childList =true
        val characterData = true
        val characterDataOldValue =true
      }.asInstanceOf[MutationObserverInit]
      contentObserver.observe(domNode, contentObserverParams)
    }

    val contentStore = <div
      class={NeptuneStyles.neptuneContent.htmlClass}
      contentEditable="true" onkeydown={preventTab _}
      mhtml-onmount={ updateContent _ }
      />

    val view = <div>{ actionBar }{ contentStore }</div>

    if (settings.styleWithCss) exec("styleWithCSS")
    Component.applyLazy(view, content)
  }

  def preventTab(kev: dom.KeyboardEvent): Unit =
    if (kev.keyCode == 9) kev.preventDefault()

}


