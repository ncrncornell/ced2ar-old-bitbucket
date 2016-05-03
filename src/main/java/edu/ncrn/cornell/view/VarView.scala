package edu.ncrn.cornell.view

import edu.ncrn.cornell.view.common.Ced2arView

import scalatags.Text.all._
import scala.collection.JavaConversions._
import scala.collection.Map


class VarView extends Ced2arView{
  
    def variableDetails(
          details : java.util.Map[Tuple2[String, Integer], String],
          handle : String,
          varname : String
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
        div(
            detailsSorted.map{case ((label,order), value) =>
              div(
              h3(label),
              p(value)
              )
            }
           )
         )
       )
      )
      typedHtml.toString()
      
   }
  
  
}