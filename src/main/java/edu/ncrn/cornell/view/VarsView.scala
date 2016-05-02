package edu.ncrn.cornell.view

import edu.ncrn.cornell.view.common.Ced2arView

import scalatags.Text.all._
import scala.collection.JavaConversions._
import scala.collection.Map


class VarsView extends Ced2arView{
  
  def varsList(
        variables : java.util.Map[String, String],
        handle : String
        ) : String = {
    val typedHtml = html(
        head(
        defaultMetaTags,
        defaultStyleSheetsAndScripts
        //script(src := "...")
      ),
        body(masterDiv(
        topBanner,
        navBar,
        h2("Variables"),
        div(
            table(
              variables.map{ case (varname, varlabl) =>
                  tr(
                     td(   
                        a(href:="/ced2ar-rdb/codebooks/"+handle+"/vars/"+varname)
                        (varname)
                     ),
                     td( varlabl)
                   )
              }.toSeq
            )
          )
        )
      )
    )
    typedHtml.toString()
  }
  
  
}