package edu.ncrn.cornell.view

import edu.ncrn.cornell.model.{Field, Schema}
import edu.ncrn.cornell.view.common.Ced2arView
import org.springframework.stereotype.Component

import scala.collection.JavaConversions._
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
          table({
            val schemas: Array[Schema] = mappingTable.keys.map{k => k._2}.toArray
            val fields: Array[Field] = mappingTable.keys.map{k => k._1}.toArray
            println(s"fields: ${fields.length}; schemas: ${schemas.length}")

            Seq(tr(// Headers first (schema identifiers)
              th(), //empty, corner entry
              schemas.map{ schema =>
                val text = s"${schema.getId.getId} ${schema.getId.getVersion}"
                th(a(href := schema.getUrl, text))
              }.toSeq
            )) ++
            // Now add field mappings as the remaining rows
            fields.map{field =>
              tr(
                Seq(td("testing")) ++ //td(field.getId), //FIXME: fix NPE
                schemas.map{schema =>
                  td(mappingTable((field, schema)))
                }.toSeq
              )
            }.toSeq
          })
        )
      ))
    )
    typedHtml.toString()
  }
  
}
