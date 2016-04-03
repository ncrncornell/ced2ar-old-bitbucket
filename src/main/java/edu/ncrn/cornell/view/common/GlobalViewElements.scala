package edu.ncrn.cornell.view.common

import scalatags.Text.all._
import scala.collection.JavaConversions._

/**
  * Created by Brandon Barker on 4/3/2016.
  */
object GlobalViewElements {

  def defaultMetaTags: Seq[Tag] = Seq(
    meta(charset := "utf-8"),
    meta(name := "viewport", content := "width=device-width, initial-scale=1")
  )

  def defaultStyleSheetsAndScripts: Seq[Tag] = {

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

    cssUrls.map{u => link( rel:= "stylesheet", `type` := "text/css", href := u)} ++
      scriptUrls.map{u => script(src := u)}
  }

  def topBanner: Seq[Tag] = {

    Seq(
      div(`class` := "row", div(`class` := "col-sm-12", style := "background-color: #B40404;",
        div(`class` := "row",
          h1(style := "color: #FFFFFF", raw("&nbsp;&nbsp;&nbsp;&nbsp;CED<sup>2</sup>AR"))
        )
      )),
      div(`class` := "row", div(`class` := "col-sm-12", raw("&nbsp;")))
    )
  }
}
