package edu.ncrn.cornell.view

import edu.ncrn.cornell.view.common.Ced2arView
import org.springframework.stereotype.Component

import scalatags.Text.all._
import scala.collection.JavaConversions._

@Component
class CodebooksView extends Ced2arView{
  
  def codebooksList(
    codebooks : java.util.Map[String, String]
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
        h2("Codebooks"),
        masterDiv(
          codebooks.map{ case (cbHandle, cbName) =>
            p(
              a(href:=s"$servletPath/codebooks/"+cbHandle)(
                cbName
              )
             )
          }.toSeq
        )
      ))
    )
    typedHtml.toString()
  }
  
  
}