package edu.ncrn.cornell.web


import edu.ncrn.cornell.view.SchemaMapView
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import edu.ncrn.cornell.model.{Field, Schema}
import edu.ncrn.cornell.model.dao.MappingDao

import scala.collection.JavaConversions._
import scala.collection.breakOut

import SchemaMapController._

/**
  * Created by Brandon Barker on 8/5/2016.
  */

@Controller
class SchemaMapController {

  @Autowired
  var view: SchemaMapView = _

  @Autowired
  var mappingDao: MappingDao = _

  //TODO: make this asynchronously update when the schema changes or cache
  def mappings: List[XpathMap] = mappingDao.findAll().map{m =>
    XpathMap(m.getField, m.getSchema, m.getXpath)
  }(breakOut)

  //GET page
  @ResponseBody
  @RequestMapping(
    method = Array(RequestMethod.GET),
    value = Array(thisPath),
    produces = Array(MediaType.TEXT_HTML_VALUE)
  )
  def schemaMap(): String = {
    
    val mappingTable: Map[(Field, Schema), String] = mappings.map{m =>
      println(s"field id: ${m.field.getId}")
      ((m.field, m.schema), m.xpath)
    }.toMap

    view.schemaMap(mappingTable)
  }

}

object SchemaMapController {

  case class XpathMap(field: Field, schema: Schema, xpath: String)

  final val thisPath = "/schema-map"
}