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
           )
         )
       )
      )
      typedHtml.toString()
      
   }
  
  
}