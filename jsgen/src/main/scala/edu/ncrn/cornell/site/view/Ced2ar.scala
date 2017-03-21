// This implementation is mostly copied from Binding.scala TodoMVC example:
// https://github.com/ThoughtWorksInc/todo/blob/master/js/src/main/scala/com/thoughtworks/todo/Main.scala
package edu.ncrn.cornell.site.view

import java.net.URI

import scala.scalajs.js.JSApp
import scala.xml.Elem
import scala.xml.Node
import mhtml._
import cats.implicits._
import mhtml.implicits.cats._
import org.scalajs.dom
import org.scalajs.dom.{DOMList, Event, KeyboardEvent}
import org.scalajs.dom.ext.KeyCode
import org.scalajs.dom.ext.LocalStorage
import org.scalajs.dom.ext.PimpedNodeList
import org.scalajs.dom.raw.HTMLInputElement
import org.scalajs.dom.raw.NodeList
import upickle.default.read
import upickle.default.write
import fr.hmil.roshttp.HttpRequest
import monix.execution.Scheduler.Implicits.global

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.{Failure, Success, Try}
import fr.hmil.roshttp.response.SimpleHttpResponse
import io.circe._
import io.circe.parser._

import scala.collection.breakOut


object Ced2ar extends JSApp {

  object Utils {

//    def domList[T](dl: DOMList[T]): List[T] = {
//      val size = dl.length
//      (0 until size).map(ii => dl.item(ii))(breakOut)
//    }

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
  //FIXME: Need to change form to not use onsubmit but just button-based events!!!!!!!!!
  class SpecifyUri(val prompt: String, formId: String, defaultUrl: String) {
    val currentUrl = Var(URI.create(defaultUrl))
    def app: (Node, Rx[URI]) = {
      val div =
        <div>
          <input type="text" placeholder={prompt}
                 id = {formId} /> <!--FIXME: make defaultUrl a constructor param -->
          <button onclick={Utils.inputEvent{iev =>
            //DEBUG:
            println("Hello from onclick")
            iev.parentElement.childNodes.toIterable.foreach(node =>
              println("nodeName is " + node.nodeName)
            )
            //DEBUG END
            val text = iev.parentElement.childNodes.toIterable
              .find(child => child.nodeName == "input") match {
              case Some(elem) => elem.nodeValue
              case None => ""
            }
            println(s"text is $text") // DEBUG
            currentUrl := URI.create(text)
          }}>OK</button>
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
          println(s"Error retrieving api info at $uriString: " + err.toString)
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
  val servletPath = "ced2ar" //TODO: make an Rx?

  val apiUriApp = new SpecifyUri(
    "Enter the API URI for the CED2AR server to use:",
    "apiUriApp",
    s"http://localhost:$defaultPort/$servletPath/api"
  )


  val currentApiUri: Rx[URI] = (checkApiUri(localApiUri)
    |@| apiUriApp.app._2.flatMap(uri => checkApiUri(uri.toString))
  ).map{
    case (Some(lUri), Some(sUri)) => sUri
    case (Some(lUri), None) => lUri
    case (None, Some(sUri)) => sUri
    case (None, None) =>  URI.create(s"http://localhost:$defaultPort/$servletPath/error")
  }


  case class Port(num: Int)

  implicit val port: Rx[Port] = currentApiUri.map{ curUri =>
    Option(curUri.getPort) match {
      case Some(prt) => Port(prt)
      case None => Port(defaultPort)
    }
  }

  currentApiUri.impure.foreach{uri => println(s"uri is ${uri.toString}")} // DEBUG
  port.impure.foreach(prt => println(s"current port is ${prt.num}")) // DEBUG


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

      val request: Rx[HttpRequest] = EndPoints.codebook(handle).map(ep =>
        HttpRequest(ep).withHeader("Content-Type", "application/javascript")
      )

      val details: Rx[List[(String, List[String])]] = request.flatMap(req =>
        Utils.fromFuture(req.send()).map {
          case Some(resTry) => resTry match {
            case res: Success[SimpleHttpResponse] =>
              decode[List[(String, List[String])]](res.get.body) match {
                case Left(detailFailure) =>
                  println("Error decoding codebook details: " + detailFailure.toString)
                  Nil
                case Right(newDetails) => newDetails
              }
            case err: Failure[SimpleHttpResponse] =>
              println("Error retrieving codebook details: " + err.toString)
              Nil
          }
          case None => Nil
        }
      )
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
        <p>
        {currentApiUri.map{curUri =>
          s"Current API URI: ${curUri.toString} with port ${curUri.getPort}"
        }}
        </p>
        {apiUriApp.app._1}
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