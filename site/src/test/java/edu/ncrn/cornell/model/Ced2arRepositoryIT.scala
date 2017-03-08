package edu.ncrn.cornell.model

import edu.ncrn.cornell.Ced2arApplication
import edu.ncrn.cornell.model.testing.BaseRepositoryIT
import edu.ncrn.cornell.service.{CodebookService, CodebookServiceTesters}
import org.junit.Test
//import org.junit.gen5.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest


/**
  * @author Brandon Elam Barker
  *         on 8/11/2016.
  */

@SpringBootTest(classes = Array(classOf[Ced2arApplication]))
//@DatabaseSetup(BaseRepositoryIT.DATASET)
//@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { BaseRepositoryIT.DATASET })
//@DirtiesContext
class Ced2arRepositoryIT extends BaseRepositoryIT with CodebookServiceTesters  {

  //TODO: refactor to services-core-tests module

  @Autowired val codeBookService: CodebookService = null

  @Test
  def getAllHandlesJsonTests: Unit = getAllHandlesJsonTests(codeBookService)

  @Test
  def getCodebookDetailsListJsonTests: Unit = getCodebookDetailsListJsonTests(codeBookService)

  @Test
  def getVariableDetailsListJsonTests: Unit = getVariableDetailsListJsonTests(codeBookService)


}
