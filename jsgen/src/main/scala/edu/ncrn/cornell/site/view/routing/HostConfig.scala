package edu.ncrn.cornell.site.view.routing

import scala.xml._
import mhtml._
import cats.implicits._
import mhtml.implicits.cats._

import scala.util.{Failure, Success}
import java.net.URI

import org.scalajs.dom
import org.scalajs.dom.html
import org.scalajs.dom.ext.PimpedNodeList

import fr.hmil.roshttp.HttpRequest
import fr.hmil.roshttp.response.SimpleHttpResponse
import monix.execution.Scheduler.Implicits.global


import edu.ncrn.cornell.site.view.utils.Utils._

object HostConfig {


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
            val text: String = ev.target match {
              case elem: dom.Element => elem.parentNode.childNodes.toIterable
                .find(child => child.nodeName === "INPUT") match {
                case Some(elem: html.Input) => elem.value.trim
                case None => defaultUrl
                case _ =>
                  println(s"Error: unrecognized element for SpecifyUri($formId)")
                  defaultUrl
              }
              case _ => println("Event error!")
                defaultUrl
            }
            currentUrl := URI.create(text) //FIXME: need to convert Unicode input to ascii
          }}>OK</button>
        </div>
      (div, currentUrl.dropRepeats)
    }
  }

  /**
    * Do a basic check to see if uri exists and is compatible
    * @param uriString
    * @return
    */
  def checkApiUri(uriString: String): Rx[Option[URI]] = {
    val uriMaybe: Rx[Option[URI]] = try {
      val uri = URI.create(stripUriHash(uriString))
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
  def schemeToDefaultPort(scheme: String) = if (scheme == "http") 8080 else 443
  val defaultPort = schemeToDefaultPort(defaultScheme)
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
  }.dropRepeats

  sealed case class Port(num: Int)
  sealed case class ServletPath(path: String)
  sealed case class Host(host: String)
  sealed case class UriScheme(scheme: String)

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
  implicit val port: Rx[Port] = (currentApiUri |@| uriScheme).map{ (curUri, curSchm) =>
    Option(curUri.getPort) match {
      case Some(prt) => if (prt < 0) Port(schemeToDefaultPort(curSchm.scheme)) else Port(prt)
      case None => Port(defaultPort)
    }
  }

  val baseUri: Rx[String] = (uriScheme |@| host |@| port |@| servletPath).map {
    (curScheme, curHost, curPort, sPath) =>
      s"${curScheme.scheme}://${curHost.host}:${curPort.num}/${sPath.path}"
  }

}
