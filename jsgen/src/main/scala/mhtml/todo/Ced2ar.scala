// This implementation is mostly copied from Binding.scala TodoMVC example:
// https://github.com/ThoughtWorksInc/todo/blob/master/js/src/main/scala/com/thoughtworks/todo/Main.scala
package mhtml.todo
import java.net.URI

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

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.util.{Failure, Success, Try}
import fr.hmil.roshttp.response.SimpleHttpResponse
import io.circe._
import io.circe.parser._



object Ced2ar extends JSApp {

  object Utils {
    def fromFuture[T](future: Future[T]): Rx[Option[Try[T]]] = {
      val result = Var(Option.empty[Try[T]])
      future.onComplete(x => result := Some(x))
      result
    }

    def inputEvent(f: HTMLInputElement => Unit): Event => Unit = {
      event: Event =>
        event.target match {
          case e: HTMLInputElement =>
            f(e)
          case _ =>
        }
    }
  }


  def localApiUri = dom.window.location.href.replaceFirst("/app", "/api")

  //TODO: move to util package or something
  class SpecifyUri(val prompt: String, formId: String, defaultUrl: String) {
    val currentUrl = Var(defaultUrl)
    def app: (Node, Rx[String]) = {
      val div =
        <div>
          <form onsubmit={Utils.inputEvent(currentUrl := _.value)}>
            <input type="text" placeholder={prompt}
                   id = {formId} value= {defaultUrl} /> <!--FIXME: make defaultUrl a constructor param -->
            <input type="submit" value="OK"/>
          </form>
          <h2>Hello {currentUrl}!</h2>
        </div>
      (div, currentUrl)
    }
  }

  /**
    * Do a basic check to see if uri exists and is compatible
    * @param uriString
    * @return
    */
  def checkApiUri(uriString: String): Rx[Option[URI]] = {
    val uriMaybe: Rx[Option[URI]] = try {
      val uri = URI.create(uriString)
      val request = HttpRequest(uriString)
        .withHeader("Content-Type", "application/javascript")
      val testResult: Var[Option[Boolean]] = Var(None)

      request.send().onComplete({
        case res: Success[SimpleHttpResponse] => testResult := Some(true)
        case err: Failure[SimpleHttpResponse] =>
          println("Error retrieving api info: " + err.toString)
          testResult := Some(false)
      })
      testResult.map {
        case None => None
        case Some(success) if success => Some(uri)
        case Some(failure) => None
      }
    } catch {
      case ex: IllegalArgumentException => Rx(None)
    }

    uriMaybe
  }

  val defaultPort = 8080

  val apiUriApp = new SpecifyUri(
    "Enter the API URI for the CED2AR server to use:",
    "apiUriApp",
    s"http://localhost:$defaultPort/ced2ar-rdb/api"
  )

  // Do a basic check to see if localApiUri exists and is compatible
  val currentApiUri: Rx[Option[URI]] = checkApiUri(localApiUri).flatMap {
    case None => apiUriApp.app._2.flatMap(url => checkApiUri(url))
    case Some(uri) => Rx(Some(uri)) // TODO prompt user if this is ok
  }

  //TODO: use currentApiUri directly or wire in from it

  case class Port(num: Int)
  implicit val port: Rx[Port] = currentApiUri.map{
    case Some(curUri) => Option(curUri.getPort) match {
      case Some(prt) => Port(prt)
      case None => Port(defaultPort)
    }
    case None => Port(defaultPort) // default
  }
  currentApiUri.map{uri => println(s"uri is ${uri.toString}")}
  port.map(prt => println(s"current port is ${prt.num}")) // DEBUG

  val servletPath = "ced2ar-rdb"
  //TODO end of TODO

  object EndPoints{
    def codebook(id: String): Rx[String] = port.map{ curPort =>
      s"http://localhost:${curPort.num}/$servletPath/codebook/$id"
    }
  }

  class Todo(val title: String, val completed: Boolean)
  object Todo {
    def apply(title: String, completed: Boolean) = new Todo(title, completed)
    def unapply(todo: Todo) = Option((todo.title, todo.completed))
  }

  case class TodoList(text: String, hash: String, items: Rx[Seq[Todo]])

  class Codebook(val handle: String) {
    def model: Rx[List[(String, List[String])]] = {
      val details: Var[List[(String, List[String])]] = Var(Nil)

      val request: Rx[HttpRequest] = EndPoints.codebook(handle).map(ep =>
        HttpRequest(ep).withHeader("Content-Type", "application/javascript")
      )

      request.impure.foreach{ req =>
        req.send().onComplete({
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
      }
      details
    }

    def view(details: Rx[List[(String, List[String])]], handle: String): Node = {
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
        {currentApiUri.map{
          case None => apiUriApp.app._1
          case Some(curUri) => <p>Current API URI: {curUri.toString} with port {curUri.getPort}</p>
          case whatsit => <p>Unknown error matching URI, got class: {whatsit.getClass.getName}</p>
        }}
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

    val testCodebook: Rx[Codebook] = currentApiUri.map{cau => new Codebook("ssbv602")}

    def index: Node = {
      <div>
        <p>Testing codebook view:</p>
          {testCodebook.map{cb =>  cb.view(cb.model, cb.handle)}}
      </div>
    }
  }

  def main(): Unit = {
    val div = dom.document.getElementById("application-container")
    mount(div, View.index)
  }
}
