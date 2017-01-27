package edu.ncrn.cornell.site.view

import edu.ncrn.cornell.site.view.common.Ced2arView
import org.springframework.stereotype.Component

import scalatags.Text.all._
import scala.collection.JavaConversions._
import scala.collection.Map
import scala.collection.immutable.ListMap


@Component
class VarsView extends Ced2arView{
  
  def varsList(
        variables : java.util.Map[String, (String,String)]
        ) : String = {
    val sortedVars = ListMap(variables.toSeq.sortBy(_._1):_*)
    val typedHtml = html(
        head(
        defaultMetaTags,
        defaultStyleSheetsAndScripts
      ),
        body(masterDiv(
        topBanner,
        navBar,
        h1("Variables"),
        masterDiv(
          masterTable(
              thead(
                  tr(
                    th("Variable Name"),
                    th("Variable Label"),
                    th("Codebook")
                   )
                  ),
              tbody(    
            sortedVars.map{ case (varname, (varlabl, handle)) =>
              tr(
                td(
                  a(href:=s"$servletPath/codebooks/"+handle+"/vars/"+varname)
                  (varname)
                ),
                td(varlabl),
                td(
                  a(href:=s"$servletPath/codebooks/"+handle)(handle)
                )
              )
            }.toSeq
          )
          )
          )
        )
      )
    )
    typedHtml.toString()
  }
  
  
}