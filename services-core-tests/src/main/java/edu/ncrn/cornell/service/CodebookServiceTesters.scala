package edu.ncrn.cornell.service

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

  def getAllHandlesIsJson(codeBookService: CodebookService): Unit = {
    val jsonHandles = codeBookService.getAllHandlesJson
    val parseResult = parse(jsonHandles)
    val jsonIsNonTrivial = parseResult match {
      case Right(someJson) =>
        println(someJson.toString()) //DEBUG
        someJson.toString().length > 6
      case Left(failure) =>
        println(failure.toString)
        false
    }
    assert(jsonIsNonTrivial)
  }

}
