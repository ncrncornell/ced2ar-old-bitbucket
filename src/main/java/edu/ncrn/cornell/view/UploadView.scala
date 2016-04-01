package edu.ncrn.cornell.view

import scalatags.Text.all._

import scala.collection.JavaConversions._


object UploadView {
  def getView(stash: java.util.Map[String, String]): String = {
    val typedHtml = html(
      head(
        script(src := "..."),
        script(
          s"alert('${stash.get("myString")}')"
        )
      ),
      body(
        div(
          h1(id := "title", "This is a title"),
          p("This is a big paragraph of text")
        )
      )
    )

    typedHtml.toString()
  }
}