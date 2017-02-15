package edu.ncrn.cornell.model

import edu.ncrn.cornell.Ced2arApplication
import edu.ncrn.cornell.model.testing.{BaseRepositoryIT, DBChecker}
import edu.ncrn.cornell.service.CodebookService
import org.junit.Test
//import org.junit.gen5.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.TestExecutionListeners


import io.circe._
import io.circe.parser._

/**
  * @author Brandon Elam Barker
  *         on 8/11/2016.
  */

@SpringBootTest(classes = Array(classOf[Ced2arApplication]))
//@DatabaseSetup(BaseRepositoryIT.DATASET)
//@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { BaseRepositoryIT.DATASET })
//@DirtiesContext
class Ced2arRepositoryIT extends BaseRepositoryIT  {

  //TODO: refactor to services-core-tests module

  @Autowired val codeBookService: CodebookService = null

  @Test
  def getAllHandlesIsJson = {
    val jsonHandles = codeBookService.getAllHandlesJson
    val parseResult = parse(jsonHandles)
    val jsonIsNonTrivial = parseResult match {
      case Right(someJson) =>
        println(someJson.toString()) //DEBUG
        someJson.toString().length > 3
      case Left(failure) =>
        println(failure.toString)
        false
    }
    assert(jsonIsNonTrivial)
  }



}
