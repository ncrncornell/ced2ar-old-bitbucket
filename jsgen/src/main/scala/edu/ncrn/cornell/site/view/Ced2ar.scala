// This implementation is mostly copied from Binding.scala TodoMVC example:
// https://github.com/ThoughtWorksInc/todo/blob/master/js/src/main/scala/com/thoughtworks/todo/Main.scala
package edu.ncrn.cornell.site.view

import java.net.URI

import scala.scalajs.js.JSApp
import scala.xml._
import mhtml._
import cats.implicits._
import mhtml.implicits.cats._
import org.scalajs.dom
import org.scalajs.dom.{DOMList, Event, KeyboardEvent, html}
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
  class SpecifyUri(val prompt: String, formId: String, defaultUrl: String) {
    val currentUrl = Var(URI.create(defaultUrl))
    def app: (Node, Rx[URI]) = {
      val div =
        <div>
          <input type="text" placeholder={prompt}
                 id = {formId} /> <!--FIXME: make defaultUrl a constructor param -->
          <button onclick={ (ev: dom.Event) => {
            val text: String = ev.srcElement.parentNode.childNodes.toIterable
              .find(child => child.nodeName === "INPUT") match {
              case Some(elem: html.Input) =>
                println("found some element")
                elem.value
              case None =>
                println("found no element")
                defaultUrl
              case _ =>
                println(s"Error: unrecognized element for SpecifyUri($formId)")
                defaultUrl
            }
            currentUrl := URI.create(text) //FIXME: need to convert Unicode input to ascii
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

  val defaultScheme = "https"
  val defaultHost = "localhost"
  val defaultPort = 8080
  val defaultServletPath = "ced2ar-web"

  val apiUriApp = new SpecifyUri(
    "Enter the API URI for the CED2AR server to use:",
    "apiUriApp",
    s"$defaultScheme://$defaultHost:$defaultPort/$defaultServletPath/api"
  )


  val currentApiUri: Rx[URI] = (checkApiUri(localApiUri)
    |@| apiUriApp.app._2.flatMap(uri => checkApiUri(uri.toString))
  ).map{
    case (Some(lUri), Some(sUri)) => sUri
    case (Some(lUri), None) => lUri
    case (None, Some(sUri)) => sUri
    case (None, None) =>
      URI.create(s"$defaultScheme://$defaultHost:$defaultPort/$defaultServletPath/api")
  }

  sealed case class Port(num: Int)
  sealed case class ServletPath(path: String)
  sealed case class Host(host: String)
  sealed case class UriScheme(scheme: String)

  implicit val port: Rx[Port] = currentApiUri.map{ curUri =>
    Option(curUri.getPort) match {
      case Some(prt) => if (prt < 0) Port(80) else Port(prt)
      case None => Port(defaultPort)
    }
  }
  implicit val servletPath: Rx[ServletPath] = currentApiUri.map{ curUri =>
    Option(curUri.getPath) match {
      case Some(path) => ServletPath(path.replaceFirst("/api", "").replaceFirst("/", ""))
      case None => ServletPath(defaultServletPath)
    }
  }
  implicit val host: Rx[Host] = currentApiUri.map{ curUri =>
    Option(curUri.getHost) match {
      case Some(hostStr) => Host(hostStr)
      case None => Host(defaultHost)
    }
  }
  implicit val uriScheme: Rx[UriScheme] = currentApiUri.map{ curUri =>
    Option(curUri.getScheme) match {
      case Some(scheme) => UriScheme(scheme)
      case None => UriScheme(defaultScheme)
    }
  }

//  currentApiUri.impure.foreach{uri => println(s"uri is ${uri.toString}")} // DEBUG
//  port.impure.foreach(prt => println(s"current port is ${prt.num}")) // DEBUG
//  servletPath.impure.foreach(spath => println(s"current spath is ${spath.path}")) // DEBUG


  object EndPoints{

    val baseUri: Rx[String] = (uriScheme |@| host |@| port |@| servletPath).map {
      (curScheme, curHost, curPort, sPath) =>
        s"${curScheme.scheme}://${curHost.host}:${curPort.num}/${sPath.path}"
    }

    def codebook(id: String): Rx[String] = baseUri.map{baseUriStr =>
        s"$baseUriStr/codebook/$id"
      }
  }

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
          s"Current API URI: ${curUri.toString}"
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
    val ced2ar = Group(Seq(Text("CED"), <sup>2</sup>, Text("AR")))

    def masterDiv(content: Node): Node = <div class="container-fluid">{content}</div>

    def masterTable(content: Node): Node =
      <table cls = "table table-striped table-hover">{content}</table>
    def indentDiv(content: Node): Node =
      <div class = "container-fluid" style = "margin-left: 3%">{content}</div>

    lazy val topBanner: Node =
      <div class = "navbar" style = "background-color: #B40404;">
        <div>
          <div style = "font-family: 'Fjord One', 'Palatino Linotype', 'Book Antiqua', Palatino, serif;">
            <h1 style = "color: #FFFFFF"><!-- FIXME: {Text("&nbsp;").text}-->{ced2ar}</h1>
            <h5 style = "color: #FFFFFF">{" " * 5 +
              "Development Server - The Comprehensive Extensible Data Documentation and Access Repository"
            }</h5>
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
            val navItems = Seq(
              "Browse by Codebook", "Browse by Variable", "Upload a Codebook",
              "Documentation", "About"
            )
            navItems.map(nItem => Group(
                <li class="divider-vertical hidden-xs"/>
                <li><a href="#">{nItem}</a></li>
            ))
          }
        </ul>
      </div>
    </nav>

    val testCodebook: Rx[Codebook] = currentApiUri.map{cau => new Codebook("ssbv602")}

    def index: Node = {masterDiv(
      <div>
        {topBanner}
        {navBar}
        {testCodebook.map{cb =>  cb.view(cb.model, cb.handle)}}
      </div>
    )}
  }

  def main(): Unit = {
    val cssUrls = Seq(
      "./target/bootstrap.min.css",
      "./target/bootstrap-theme.min.css"
    )
    dom.document.getElementsByTagName("head").headOption match {
      case Some(head) =>
        val linkRelCss =
         Group(cssUrls.map(cssUrl => <link rel="stylesheet" href={cssUrl}/>))
        mount(head, linkRelCss)
      case None => println("WARNING: no <head> element in enclosing document!")
    }

    val div = dom.document.getElementById("application-container")
    mount(div, View.index)
  }
}
