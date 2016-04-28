package edu.ncrn.cornell.view.common

import org.springframework.web.servlet.mvc.support.RedirectAttributes

import scalatags.Text.all._
import scalatags.Text.tags2.{nav}

import scala.collection.JavaConversions._

/**
  * Created by Brandon Barker on 4/25/2016.
  */
trait Ced2arView {

  // A few elements needed by almost every view:

  val defaultMetaTags: Seq[Tag] = Seq(
    meta(charset := "utf-8"),
    meta(name := "viewport", content := "width=device-width, initial-scale=1")
  )

  val defaultStyleSheetsAndScripts: Seq[Tag] = {
    val cssUrls = Seq(
      "styles/main.css",
      "//netdna.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css",
      "//netdna.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap-theme.min.css",
      "http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css"
    )
    val scriptUrls = Seq(
      "https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js",
      "http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"
    )
    val googleFontBase = "https://fonts.googleapis.com/css?family="
    val googleFontUrls = Seq(
      "Fjord+One"
    ).map(u => googleFontBase + u)

    cssUrls.map{u => link( rel:= "stylesheet", `type` := "text/css", href := u)} ++
      scriptUrls.map{u => script(src := u)} ++
      googleFontUrls.map{u => link( rel:= "stylesheet", `type` := "text/css", href := u) }
  }

  val masterDiv = div(`class` := "container-fluid")
  
  val topBanner: Seq[Tag] = Seq(
    div(`class` := "navbar", style := "background-color: #B40404;", div(
      // TODO: factor below font-family out to headings css class using ScalaCSS
      div(style := "font-family: 'Fjord One', 'Palatino Linotype', 'Book Antiqua', Palatino, serif;",
        h1(style := "color: #FFFFFF", raw("&nbsp;&nbsp;&nbsp;&nbsp;CED<sup>2</sup>AR"))
      ),
      div(`class` := "row", div(`class` := "col-sm-12", raw("&nbsp;")))
    ))
  )

  val navBar: Tag = nav(`class` := "navbar navbar-inverse",
    div(`class` := "navbar-collapse",
      ul(`class` := "nav navbar-nav",
        li(`class` := "divider-vertical hidden-xs"),
        li(`class` := "dropdown",
          a(href := "#", `class` := "dropdown-toggle",
            "data-toggle".attr := "dropdown",
            raw("Browse Variables&nbsp;<b class=\"caret\"></b>")
          ),
          ul(`class` := "dropdown-menu",
            li(a(href := "/all", "View All")),
            li(a(href := "/browse", "Sort Alphabetically")),
            li(a(href := "/groups", "Sort by Group"))
          ),
          li(`class` := "divider-vertical hidden-xs"),
          li(a(href := "/codebooks", "Browse by Codebook")),
          li(`class` := "divider-vertical hidden-xs"),
          li(a(href := "/docs", "Documentation")),
          li(`class` := "divider-vertical hidden-xs"),
          li(a(href := "/about", "About"))
        )
      )
    )
  )

  // Attribute handling logic:

  var model: Option[RedirectAttributes] = None

  def setAttributes(model: RedirectAttributes) = {
    this.model = Some(model)
  }

  val messageClassMap: Map[String, String] = Map(
    "error"   -> "alert alert-danger",
    "message" -> "alert alert-info",
    "success" -> "alert alert-success",
    "warning" -> "alert alert-warning"
  )

  def messages: Seq[Tag] = model match {
    case Some(m) => messageClassMap.keys.flatMap(mKey =>
      if (m.getFlashAttributes.containsKey(mKey))
        Seq(div(
          `class` := messageClassMap(mKey),
          m.getFlashAttributes.getOrElse(mKey, mKey).toString )
        )
      else Nil
    ).toSeq
    case None => Nil
  }


}
