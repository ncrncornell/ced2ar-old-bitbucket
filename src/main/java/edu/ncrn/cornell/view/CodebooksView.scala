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
        defaultStyleSheetsAndScripts,
        script(src := "...")
      ),
        body(
        topBanner,
        h2("Codebooks"),
        div(
            
            codebooks.foreach{ case (handle, name) =>
              p(
                  a(href:="localhost:8080/ced2ar-rdb/codebooks/"+handle)(
                    name
                    )
               )
            }
        ) 
      )
    )
    typedHtml.toString()
  }
  
  
}