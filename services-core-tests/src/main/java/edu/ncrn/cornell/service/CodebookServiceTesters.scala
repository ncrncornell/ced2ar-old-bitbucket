package edu.ncrn.cornell.service

import edu.ncrn.cornell.service.JsonComparison._

import org.junit.gen5.api.Assertions._
import org.junit.gen5.api.AfterEach
import org.junit.gen5.api.BeforeAll
import org.junit.gen5.api.BeforeEach
import io.circe._
import io.circe.parser._

/**
  * @author Brandon Elam Barker
  *         2/16/2017
  */


/**
  * Tester methods that can be run as either unit tests
  * or integration tests
  */
trait CodebookServiceTesters {

  def getAllHandlesJsonTests(codeBookService: CodebookService): Unit = {
    val jsonHandles = codeBookService.getAllHandlesJson
    val parseResult = parse(jsonHandles)
    // Check that we probably got some JSON back ...
    val (json, jsonIsNonTrivial): (String, Boolean) = parseResult match {
      case Right(someJson) =>
        println(someJson.toString()) //DEBUG
        (someJson.toString(), someJson.toString().length > 6)
      case Left(failure) =>
        println(failure.toString)
        ("", false)
    }
    assert(jsonIsNonTrivial)
    //Check we got back ssbv602, fairly standard in our tests
    assert(compareJsonLenient("""{"ssbv602" : "SIPP Synthetic Beta v6.02"}""", json))

  }

}
