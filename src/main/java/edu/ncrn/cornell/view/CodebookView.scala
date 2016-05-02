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
      val deets = details.toSeq
      deets.sortBy(_._1._2)
      //println(deets) //not sorting???
      val typedHtml = html(
          head(
        defaultMetaTags,
        defaultStyleSheetsAndScripts
        //script(src := "...")
      ),
        body(masterDiv(
        topBanner,
        navBar,
        div(
            deets.map{case ((label,order), value) =>
              div(
              h3(label),
              p(value)
              )
            }//.toSeq //sort by order
           ),
        
        a(href := "localhost:8080/ced2ar-rdb/codebooks/"+handle+"/vars")(
           p("View Variables")
           )
         )
       )
      )
      typedHtml.toString()
      
   }
  
  
}