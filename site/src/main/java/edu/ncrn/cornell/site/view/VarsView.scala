package edu.ncrn.cornell.site.view

import edu.ncrn.cornell.service.api.VarNames
import edu.ncrn.cornell.site.view.common.Ced2arView
import org.springframework.stereotype.Component

import scalatags.Text.all._
import scala.collection.immutable.ListMap


@Component
class VarsView extends Ced2arView{
  
  def varsList(variables : VarNames) : String = {
    val sortedVars = ListMap(variables.sortBy(_._1):_*)
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
                  a(href:=s"$servletPath/codebook/"+handle+"/var/"+varname)
                  (varname)
                ),
                td(varlabl),
                td(
                  a(href:=s"$servletPath/codebook/"+handle)(handle)
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