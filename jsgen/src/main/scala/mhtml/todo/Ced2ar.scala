// This implementation is mostly copied from Binding.scala TodoMVC example:
// https://github.com/ThoughtWorksInc/todo/blob/master/js/src/main/scala/com/thoughtworks/todo/Main.scala
package mhtml.todo
import scala.scalajs.js.JSApp
import scala.xml.Elem
import scala.xml.Node

import mhtml._
import org.scalajs.dom
import org.scalajs.dom.Event
import org.scalajs.dom.KeyboardEvent
import org.scalajs.dom.ext.KeyCode
import org.scalajs.dom.ext.LocalStorage
import org.scalajs.dom.raw.HTMLInputElement
import upickle.default.read
import upickle.default.write

import fr.hmil.roshttp.HttpRequest
import monix.execution.Scheduler.Implicits.global
import scala.util.{Failure, Success}
import fr.hmil.roshttp.response.SimpleHttpResponse

import io.circe._, io.circe.parser._

object Ced2ar extends JSApp {

  //TODO: use something standard and wire in from config somehow, if it exists
  case class Port(num: Int)
  implicit val port = Port(8081)

  object EndPoints{
    def codebook(id: String)(implicit port: Port) =
      s"http://localhost:${port.num}/ced2ar-rdb/codebook/$id"
  }

  class Todo(val title: String, val completed: Boolean)
  object Todo {
    def apply(title: String, completed: Boolean) = new Todo(title, completed)
    def unapply(todo: Todo) = Option((todo.title, todo.completed))
  }

  case class TodoList(text: String, hash: String, items: Rx[Seq[Todo]])

  class Codebook(val id: String) {
    //TODO: need to figure out monix API
//    def model: List[(String, List[String])] = {
//      val request = HttpRequest(EndPoints.codebook(id))
//
//
//      val detailsStr: String = request.send().onComplete({
//        case res: Success[SimpleHttpResponse] =>
//          res.get.body
//          ""
//        case err: Failure[SimpleHttpResponse] =>
//          println(s"Error retrieving codebook: $id")
//          ""
//      })
//      val details: List[(String, List[String])] = parse(detailsStr)
//    }

    def view: Node = {
      <div>

      </div>
    }

  }

  object View {
    def index: Node = {
      <div>
        <section class="todoapp">Hello World</section>
        <!-- <section class="todoapp">{header}{mainSection}{footer}</section> -->
        <footer class="info">
          <p>Double-click to edit a todo</p>
          <p>
            Originally written by <a href="https://github.com/atry">Yang Bo</a>,
            adapted to monadic-html by <a href="https://github.com/olafurpg">Olafur Pall Geirsson</a>.
          </p>
          <p>Part of <a href="http://todomvc.com">TodoMVC</a></p>
        </footer>

        <p>Testing codebook view:</p>

      </div>
    }
  }

  def main(): Unit = {
    val div = dom.document.getElementById("application-container")
    mount(div, View.index)
  }
}
