package edu.ncrn.cornell.service

import edu.ncrn.cornell.model._
import edu.ncrn.cornell.model.dao._
import io.circe.syntax._
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.{Page, PageRequest, Pageable, Sort}
import org.springframework.stereotype.Service

import scala.collection.JavaConverters._
import scala.collection.mutable

/**
  * Provides version and other high-level informationa bout the service
  *
  * @author Brandon Elam Barker
  *
  */
@Service
@Autowired
class ApiInfoService() {

//TODO: since we can't read site properties in the services modules, we can't read Ced2arConfig
//TODO: instead see http://stackoverflow.com/questions/3697449/retrieve-version-from-maven-pom-xml-in-code
//TODO: the relevant path is META-INF/MANIFEST.MF : Implementation-Version

//TODO: do correctly using api module
//  case class Version(major: Int, minor: Int, patch: Int)
//  val API_VERSION = Version(0, 0, 1)

  val API_VERSION = "0.0.1"
  /**
    *
    *
    * @return Map of key, value pairs including API version,
    *         system version
    */
  def apiInfo: Map[String, String] = {
    val info = Map(
      "version-impl" -> "not.a.real.version",
      "version-api" -> API_VERSION
    )
    info
  }

  def apiInfoJson: String = apiInfo.asJson.noSpaces


  //TODO: add an endpoints service? would be interesting to provide serialized types
  // for at least Scala as well

}