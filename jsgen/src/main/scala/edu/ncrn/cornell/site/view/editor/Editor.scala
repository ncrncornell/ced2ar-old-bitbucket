package edu.ncrn.cornell.site.view.editor

import org.scalajs.dom
import dom.{document, window}
import dom.raw.MouseEvent
import edu.ncrn.cornell.site.view.component.Component
import mhtml.{Rx, Var}

import scala.scalajs.js.annotation.JSExportTopLevel
import scala.xml.{EntityRef, Group, Node, Text}

/**

  Adapted from https://github.com/amirkarimi/neptune

  MIT license follows:

  Copyright (c) 2017 Amir Karimi

  Permission is hereby granted, free of charge, to any person obtaining a copy
  of this software and associated documentation files (the "Software"), to deal
  in the Software without restriction, including without limitation the rights
  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  copies of the Software, and to permit persons to whom the Software is
  furnished to do so, subject to the following conditions:

  The above copyright notice and this permission notice shall be included in all
  copies or substantial portions of the Software.

  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  SOFTWARE.
  */

@JSExportTopLevel("Editor")
object Editor {
  case class Action(icon: Node, title: String, command: () => Unit)

  object Act {
    def apply(icon: Node, title: String)(command: => Unit): Action = {
      Action.apply(icon, title, () => command)
    }
  }

  case class Settings(actions: Seq[Action] = actions, styleWithCss: Boolean = false)

  val actions = Seq(
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
    Act(<div>&lt;/&gt;</div>, "Code") {
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

  //TODO: To be a replacement for neptune editor, but in mhtml
  def editor(settings: Settings = Settings()): Component[Node] = {
    // TODO: settings.classes.actionbar
    val actionBar = <div class={NeptuneStyles.neptuneActionbar.htmlClass}>{
      actions.map { action =>
        <button class={ NeptuneStyles.neptuneButton.htmlClass }
          title={ action.title }
          onclick={ (ev: MouseEvent) => action.command() }
        >{ action.icon }</button>
      }
    }</div>

    val content =
      Rx(<div class={NeptuneStyles.neptuneContent.htmlClass}
           contentEditable="true" onkeydown={preventTab _} />)

    val view = <div>{actionBar}{content}</div>

    //if (settings.styleWithCss) exec("styleWithCSS")
    exec("styleWithCSS")
    Component(view, content)
  }


  def preventTab(kev: dom.KeyboardEvent): Unit =
    if (kev.keyCode == 9) kev.preventDefault()

}

