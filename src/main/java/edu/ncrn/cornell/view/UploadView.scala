package edu.ncrn.cornell.view

import org.springframework.ui.Model
import scalatags.Text.all._

object UploadView {
  def getView(model: Model): String = {
    val typedHtml = html(
      head(
        script(src := "..."),
        script(
          "alert('Hello World')"
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