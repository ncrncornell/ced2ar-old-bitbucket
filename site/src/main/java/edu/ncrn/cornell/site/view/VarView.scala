package edu.ncrn.cornell.site.view

import edu.ncrn.cornell.site.view.common.Ced2arView
import org.springframework.stereotype.Component

import scalatags.Text.all._
import scala.collection.JavaConversions._
import scala.collection.Map

@Component
class VarView extends Ced2arView{
  
    def variableDetails(
       details : java.util.Map[(String, Integer), String],
       handle : String,
       varname : String
    ) : String = {
      val detailsSorted = details.toSeq.sortBy(_._1._2)
      val typedHtml = html(
        head(
          defaultMetaTags,
          defaultStyleSheetsAndScripts,
          //Mathjax is pulled from networked resource, this will not work in restricted environments.
          script(`type` := "text/javascript", `src` := "https://cdn.mathjax.org/mathjax/latest/MathJax.js?config=AM_CHTML")
      ),
        body(masterDiv(
          topBanner,
          navBar,
          ol(cls:="breadcrumb")(
            li(a(href:= s"$servletPath/codebooks")("Codebooks")),
            li(a(href:= s"$servletPath/codebooks/"+handle)(handle)),
            li(a(href:= s"$servletPath/codebooks/"+handle+"/vars")("Variables")),
            li(cls:="active")(varname)
          ),
          masterDiv(
            detailsSorted.map{case ((vLabel,order), vValue) =>
              div(
              h3(vLabel),
              p(vValue)
              )
            }
          )
        ))
      )
      typedHtml.toString()
      
   }
  
  
}