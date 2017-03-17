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
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.{Failure, Success}
import fr.hmil.roshttp.response.SimpleHttpResponse

import io.circe._, io.circe.parser._

object Ced2ar extends JSApp {

  //TODO: use something standard and wire in from config somehow, if it exists
  case class Port(num: Int)
  implicit val port = Port(8080)
  val servletPath = "ced2ar"
  //TODO end of TODO

  object EndPoints{
    def codebook(id: String)(implicit port: Port) =
      s"http://localhost:${port.num}/$servletPath/codebook/$id"
  }

  class Todo(val title: String, val completed: Boolean)
  object Todo {
    def apply(title: String, completed: Boolean) = new Todo(title, completed)
    def unapply(todo: Todo) = Option((todo.title, todo.completed))
  }

  case class TodoList(text: String, hash: String, items: Rx[Seq[Todo]])

  class Codebook(val handle: String) {
    def model: Var[List[(String, List[String])]] = {
      val details: Var[List[(String, List[String])]] = Var(Nil)

      val request = HttpRequest(EndPoints.codebook(handle))
        .withHeader("Content-Type", "application/javascript")

      request.send().onComplete({
        case res: Success[SimpleHttpResponse] =>
          details := (decode[List[(String, List[String])]](res.get.body) match {
            case Left(detailFailure) =>
              println("Error decoding codebook details: " + detailFailure.toString)
              Nil
            case Right(newDetails) => newDetails
          })
        case err: Failure[SimpleHttpResponse] =>
          println("Error retrieving codebook details: " + err.toString)
          details := Nil
      })
      details
    }

    def view(details: Var[List[(String, List[String])]], handle: String): Node = {
      val collapsibleFields = Set("Files")

      def renderField(fieldName: String, fieldValues: List[String]): Node = {
        if (collapsibleFields.contains(fieldName))
          <div>
            <h3>
              <a class="glyphicon glyphicon-menu-down"
                 href={s"#$fieldName-detail"} data-toggle="collapse">
                {fieldName}
              </a>
              <p id={s"$fieldName-detail"} class="collapse">
                {fieldValues.mkString("\n")}
              </p>
            </h3>
          </div>
        else
          <div>
            <h3>
              {fieldName}
            </h3>
            <p>
              {fieldValues.mkString("\n")}
            </p>
          </div>
      }

      <div>
        <ol cls="breadcrumb">
          <li><a href={s"codebook"}></a>Codebooks</li>
          <li cls="active">{handle}</li>
        </ol>
        <div>
          <a href={s"codebook/$handle/var"}>View Variables</a>
        </div>
        <div>
          {details.map(cd => cd.map{
            case (fieldName, fieldValues) => renderField(fieldName, fieldValues)
          })}
        </div>
      </div>

    }
  }

  object View {

     val testCodebook = new Codebook("ssbv602")

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
          {testCodebook.view(testCodebook.model, testCodebook.handle)}
      </div>
    }
  }

  def main(): Unit = {
    val div = dom.document.getElementById("application-container")
    mount(div, View.index)
  }
}
