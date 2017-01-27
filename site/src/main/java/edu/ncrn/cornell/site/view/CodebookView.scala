package edu.ncrn.cornell.site.view

import edu.ncrn.cornell.site.view.common.Ced2arView
import org.springframework.stereotype.Component

import scalatags.Text.all._


@Component
class CodebookView extends Ced2arView {

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
        masterDiv(
            a(href := s"$servletPath/codebooks/$handle/vars")
         ("View Variables")
         ),
         masterDiv(
          detailsSorted.map{case ((fieldName, order), fieldValue) =>
            div(
              h3(fieldName),
              p(fieldValue)
            )
          }
        )
         )
       )
      )
      typedHtml.toString()
      
   }
  
  
}