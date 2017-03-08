package edu.ncrn.cornell.site.view

import edu.ncrn.cornell.site.view.common.Ced2arView
import org.springframework.stereotype.Component

import scalatags.Text.TypedTag
import scalatags.Text.all._


@Component
class CodebookView extends Ced2arView {

  val collapsableFields = Set("Files")

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
            li(a(href:= s"$servletPath/codebooks")("Codebooks")),
            li(cls:="active")(handle)
          ),
          div(
            a(href := s"$servletPath/codebooks/$handle/vars")
           ("View Variables")
          ),
          div(
            details.map{case (fieldName, fieldValues) =>
              renderField(fieldName, fieldValues)
            }
          )
        )
      ),
      div(id := "application-container"),
      script(`type` := "application/javascript",
        "mhtml.todo.MhtmlTodo().main();"
      )
    )
    typedHtml.toString()
  }

  private def renderField(fieldName: String, fieldValues: List[String]): TypedTag[String] =
    if (collapsableFields.contains(fieldName))
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