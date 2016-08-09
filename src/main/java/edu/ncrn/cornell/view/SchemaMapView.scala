package edu.ncrn.cornell.view

import edu.ncrn.cornell.model.{Field, Schema}
import edu.ncrn.cornell.view.common.Ced2arView
import org.springframework.stereotype.Component

import scalatags.Text.all._

@Component
class SchemaMapView extends Ced2arView{

  
  def schemaMap(
       mappingTable : Map[(Field, Schema), String]
  ) : String = {
    val typedHtml = html(
        head(
        defaultMetaTags,
        defaultStyleSheetsAndScripts//,
      ),
      body(masterDiv(
        topBanner,
        navBar,
        ol(cls:="breadcrumb")(
          li(cls:="active")("Schema Map")
        ),
        h2("Schema Map"),
        masterDiv(
          p(
            ced2ar, " stores metadata in a database table that maps to XML files written in various schemas and allows for conversion between schema type. In order to perform this conversion, we have an internal ", em("Field"),
            " that maps to particular locations (",
            a(href:="https://en.wikipedia.org/wiki/XPath", "XPaths"), s") within the XML document for each supported schema. These mappings are shown below for each Schema currently supported in ", ced2ar, "."
          ),
          div(`class` := "table-responsive", table(`class` := "table", {
            val schemas: Array[Schema] = mappingTable.keys.map{k => k._2}.toArray
            val fields: Array[Field] = mappingTable.keys.map{k => k._1}.toArray
            Seq(tr(// Headers first (schema identifiers)
              th(b("Field")),
              schemas.map{ schema =>
                val text = s"${schema.getId.getId} ${schema.getId.getVersion}"
                th(a(href := schema.getUrl, text))
              }.toSeq
            )) ++
            // Now add field mappings as the remaining rows
            fields.map{field =>
              tr(
                Seq(td(field.getId)) ++
                schemas.map{schema =>
                  td(mappingTable.getOrElse((field, schema), ""): String)
                }.toSeq
              )
            }.toSeq
          }))
        )
      ))
    )
    typedHtml.toString()
  }
  
}
