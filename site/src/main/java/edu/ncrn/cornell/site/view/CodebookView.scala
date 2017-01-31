package edu.ncrn.cornell.site.view

import edu.ncrn.cornell.site.view.common.Ced2arView
import org.springframework.stereotype.Component

import scalatags.Text.TypedTag
import scalatags.Text.all._


@Component
class CodebookView extends Ced2arView {

  val collapsableFields = Set("Files")

  def codebookDetails(
     details : Map[(String, Int), String],
     handle : String
  ) : String = {
    val detailsSorted = details.toSeq.sortBy(_._1._2)
    val typedHtml = html(
      head(
        defaultMetaTags,
        defaultStyleSheetsAndScripts
      ),
      body(masterDiv(
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
          detailsSorted.map{case ((fieldName, order), fieldValue) =>
            renderField(fieldName, fieldValue)
          }
        )
      ))
    )
    typedHtml.toString()
  }

  private def renderField(fieldName: String, fieldValue: String): TypedTag[String] =
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
          fieldValue
        )
      )
    else
      div(
        h3(fieldName),
        p(fieldValue)
      )
  
}