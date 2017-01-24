package edu.ncrn.cornell.site.view

import edu.ncrn.cornell.site.view.common.Ced2arView
import org.springframework.stereotype.Component

import scalatags.Text.all._
import scala.collection.JavaConversions._

/**
  * Created by Brandon Barker on 4/1/2016.
  */

@Component
class UploadView extends Ced2arView {
  //TODO: add flash messages
  def uploadForm(
    uploadedFiles: java.util.List[String]
  ): String = {
    val typedHtml = html(
      head(
        defaultMetaTags,
        defaultStyleSheetsAndScripts
      ),
      body(masterDiv(
        topBanner,
        navBar,
        messages,
        div(form(
          method := "POST",
          action := s"$servletPath/upload",
          enctype := "multipart/form-data",
          table(
            tr(
              td("File to upload:"),
              td(input(`type` := "file", name := "file"))
            ),
            tr(
              td("Name:"),
              td(input(`type` := "text", name := "name"))
            ),
            tr(
              td(), td(input(`type` := "submit", value := "Upload"))
            )
          )
        )),
        div(
          h2("Files already uploaded:"),
          ul(
            uploadedFiles.map{f => li(f)}
          )

        )
      )
    ))

    typedHtml.toString()
  }

  def noUploadDir(invalidDir: String): String = {
    val typedHtml = html(
      head(
        script(src := "...")
      ),
      body(
        div(
          //TODO: Need to come up with some canned administrator message/template
          h2(s"Error: specified upload directory '$invalidDir' not present! " +
              "Please contact administrators.")
        )
      )
    )

    typedHtml.toString()
  }


}