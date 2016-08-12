package edu.ncrn.cornell.model.testing;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import org.junit.Assert;
//import org.junit.gen5.api.Test; //TODO: doesn't work with spring yet
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import static org.junit.Assert.assertTrue;

/**
 * Created by Brandon Barker on 8/8/2016.
 */

@TestExecutionListeners({ DbUnitTestExecutionListener.class})
//@SpringApplicationConfiguration(classes = Ced2arApplication.class)
//@DatabaseSetup(BaseRepositoryIT.DATASET)
//@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { BaseRepositoryIT.DATASET })
@DirtiesContext
/**
 * This class is meant to be used in actual application code that
 * has a db instance to test on.
 */
abstract public class BaseRepositoryIT extends AbstractTransactionalJUnit4SpringContextTests
{
    //TODO: construct a good test-dataset, e.g., see:
    // http://g00glen00b.be/testing-spring-data-repository/

    @Autowired
    protected DBChecker dbChecker;

    private boolean dBInitializationWasTested = false;

    @Test
    public void initializeDbIfNeeded(){
        if (!dBInitializationWasTested) {
            Assert.assertTrue(dbChecker.DBinitIsOk());
            dBInitializationWasTested = true;
        }
    }


}
