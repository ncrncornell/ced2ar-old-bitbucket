package edu.ncrn.cornell.view

import scalatags.Text.all._

import scala.collection.JavaConversions._


object UploadView {
  def getView(uploadedFiles: java.util.List[String]): String = {
    val typedHtml = html(
      head(
        script(src := "...")
      ),
      body(
        div(
          h2("Files already uploaded"),
          ul(
            uploadedFiles.map{f => li(f)}
          )

        )
      )
    )

    typedHtml.toString()
  }

  def noUploadDir(): String = {
    val typedHtml = html(
      head(
        script(src := "...")
      ),
      body(
        div(
          //TODO: Need to come up with some canned administrator message/template
          h2("Error: specified upload directory not present! Please contact administrators.")
        )
      )
    )

    typedHtml.toString()
  }
}