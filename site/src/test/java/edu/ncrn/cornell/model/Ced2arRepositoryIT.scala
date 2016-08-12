package edu.ncrn.cornell.model

import edu.ncrn.cornell.Ced2arApplication
import edu.ncrn.cornell.model.testing.{BaseRepositoryIT, DBChecker}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.TestExecutionListeners

/**
  * @author Brandon Elam Barker
  *         on 8/11/2016.
  */

@SpringApplicationConfiguration(classes = Array(classOf[Ced2arApplication]))
//@DatabaseSetup(BaseRepositoryIT.DATASET)
//@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { BaseRepositoryIT.DATASET })
//@DirtiesContext
class Ced2arRepositoryIT extends BaseRepositoryIT {



}
