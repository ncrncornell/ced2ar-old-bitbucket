package edu.ncrn.cornell.view

import edu.ncrn.cornell.web.DocsController._
import edu.ncrn.cornell.model.{Field, Schema}
import edu.ncrn.cornell.view.common.Ced2arView
import edu.ncrn.cornell.view.DocsView._
import org.springframework.stereotype.Component

import scalatags.Text.all._

@Component
class DocsView extends Ced2arView{

  
  def showDocs(
       userGuides: List[UrlWithText], externalUrls: List[UrlWithText]
  ) : String = {
    val typedHtml = html(
        head(
        defaultMetaTags,
        defaultStyleSheetsAndScripts//,
      ),
      body(masterDiv(
        topBanner,
        navBar,
        ol(cls:="breadcrumb")(
          li(cls:="active")("Documentation")
        ),
        h2("Documentation"),
        masterDiv(
          div(`class` := "table-responsive", table(`class` := "table", {
            Seq(tr(// Headers first
              th(h4("User Guides")), th(h4("External Resources"))
            )) ++
            // Now fill in row data:
            userGuides.zipAll(externalUrls, noUrl, noUrl).map{rowData =>
              tr(th(toTag(rowData._1)), th(toTag(rowData._2)))
            }
          }))
        )
      ))
    )
    typedHtml.toString()
  }
  
}

object DocsView {
  def toTag(ut: UrlWithText) =
    if (ut.url != "" && ut.text != "") a(href := ut.url, ut.text)
    else raw("")
}
