package edu.ncrn.cornell.service

import edu.ncrn.cornell.service.JsonComparison._
import io.circe.Json.JArray
import org.junit.gen5.api.Assertions._
import org.junit.gen5.api.AfterEach
import org.junit.gen5.api.BeforeAll
import org.junit.gen5.api.BeforeEach
import io.circe._
import io.circe.Json.JArray
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
        (someJson.toString(), someJson.toString().length > 6)
      case Left(failure) =>
        ("", false)
    }
    assert(jsonIsNonTrivial)
    //Check we got back ssbv602, fairly standard in our tests
    assert(compareJsonLenient("""{"ssbv602" : "SIPP Synthetic Beta v6.02"}""", json))
  }

  def getCodebookDetailsListJsonTests(codeBookService: CodebookService): Unit = {
    val details = codeBookService.getCodebookDetailsListJson("ssbv602")
    detailsCheckerJson(details)
  }

  def getVariableDetailsListJsonTests(codeBookService: CodebookService): Unit = {
    val details = codeBookService.getVariableDetailsListJson("ssbv602", "afdc_M")
    detailsCheckerJson(details)
  }

  //
  // Utility functions follow (not individual testers)
  //

  // JSON should conform to shape List[(String, List[String])]
  def detailsCheckerJson(details: String): Unit = {
    val parseResult = parse(details)
    // Check that we probably got some JSON back ...
    val (json, jsonIsNonTrivial): (Json, Boolean) = parseResult match {
      case Right(someJson) =>
        (someJson, someJson.toString().length > 6)
      case Left(failure) =>
        (Json.Null, false)
    }
    assert(jsonIsNonTrivial)

    // Should be an non-empty array in JSON
    assert(json.isArray)
    val jsonArray: Vector[Json] = json.asArray match {
      case Some(jArray) => jArray
      case None => Vector()
    }
    assert(jsonArray.nonEmpty)

    // Check we got back non-trivial values for ssbv602
    val detailValues: Vector[Vector[Json]] = jsonArray.map{jsn =>
      jsn.asArray match {
        case Some(jsonTuple) =>
          // Values are in second array position:
          jsonTuple(1).asArray match {
            case Some(jsonArrayValues) =>
              jsonArrayValues
            case _ => Vector()
          }
        case None => Vector[Json]()
      }}
    assert(detailValues.nonEmpty)
    assert(detailValues.forall(vec => vec.nonEmpty))

  }


}
