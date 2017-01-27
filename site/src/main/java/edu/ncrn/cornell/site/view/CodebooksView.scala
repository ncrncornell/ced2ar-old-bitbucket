package edu.ncrn.cornell.site.view

import edu.ncrn.cornell.site.view.common.Ced2arView
import org.springframework.stereotype.Component

import scalatags.Text.all._

@Component
class CodebooksView extends Ced2arView{
  
  def codebooksList(
    codebooks : Map[String, String]
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
          li(cls:="active")("Codebooks")
        ),
        indentDiv(h1("Codebooks"),
        masterDiv(
          codebooks.map{ case (cbHandle, cbName) =>
            p(
              a(href:=s"$servletPath/codebooks/"+cbHandle)(
                cbName
              )
             )
          }.toSeq
        )
      )))
    )
    typedHtml.toString()
  }
  
  
}