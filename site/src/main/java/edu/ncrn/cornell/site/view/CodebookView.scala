package edu.ncrn.cornell.site.view

import edu.ncrn.cornell.site.view.common.Ced2arView
import org.springframework.stereotype.Component

import scalatags.Text.TypedTag
import scalatags.Text.all._


@Component
class CodebookView extends Ced2arView {

  val collapsibleFields = Set("Files")

  def codebookDetails(
     details: List[(String, List[String])],
     handle: String
  ) : String = {
    val typedHtml = html(
      head(
        defaultMetaTags,
        defaultStyleSheetsAndScripts
      ),
      body(
        masterDiv(
          topBanner,
          navBar,
          ol(cls:="breadcrumb")(
            li(a(href:= s"$servletPath/codebook")("Codebooks")),
            li(cls:="active")(handle)
          ),
          div(
            a(href := s"$servletPath/codebook/$handle/var")
           ("View Variables")
          ),
          div(
            details.map{case (fieldName, fieldValues) =>
              renderField(fieldName, fieldValues)
            }
          )
        )
      )
    )
    typedHtml.toString()
  }

  //TODO: if we are thinking of using the SPA as the main interface, we should probably
  //TODO: avoid all use of interactive javascript in returned, static html views
  private def renderField(fieldName: String, fieldValues: List[String]): TypedTag[String] =
    if (collapsibleFields.contains(fieldName))
      div(
        h3(
          a(`class` := "glyphicon glyphicon-menu-down",
            href := s"#$fieldName-detail",
            attr("data-toggle") := "collapse",
            fieldName
          )
        ),
        p(id := s"$fieldName-detail", `class` := "collapse",
          fieldValues.mkString("\n")
        )
      )
    else
      div(
        h3(fieldName),
        p(fieldValues.mkString("\n"))
      )
  
}