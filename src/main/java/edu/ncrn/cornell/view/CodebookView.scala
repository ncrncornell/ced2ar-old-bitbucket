package edu.ncrn.cornell.view

import edu.ncrn.cornell.view.common.Ced2arView

import scalatags.Text.all._
import scala.collection.JavaConversions._
import scala.collection.Map


class CodebookView extends Ced2arView{
  
    def codebookDetails(
          details : java.util.Map[Tuple2[String, Integer], String],
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
            li(a(href:= "/ced2ar-rdb/codebooks")("Codebooks")),
            li(cls:="active")(handle)
        ),
        masterDiv(
            detailsSorted.map{case ((label,order), value) =>
              div(
              h3(label),
              p(value)
              )
            }
           ),
          a(href := "/ced2ar-rdb/codebooks/"+handle+"/vars")
           ("View Variables")
         )
       )
      )
      typedHtml.toString()
      
   }
  
  
}