package edu.ncrn.cornell.view

import edu.ncrn.cornell.view.common.Ced2arView

import scalatags.Text.all._
import scala.collection.JavaConversions._
import scala.collection.Map


class CodebooksView extends Ced2arView{
  
  def codebooksList(
            codebooks : java.util.Map[String, String]
      ) : String = {
    val typedHtml = html(
        head(
        defaultMetaTags,
        defaultStyleSheetsAndScripts//,
        //script(src := "...")
      ),
        body(masterDiv(
        topBanner,
        navBar,
        h2("Codebooks"),
        div(
          codebooks.map{ case (handle, cname) =>
            p(
              a(href:="/codebooks/"+handle)(
                cname
              )
             )
          }.toSeq
        ) 
      )
    ))
    typedHtml.toString()
  }
  
  
}