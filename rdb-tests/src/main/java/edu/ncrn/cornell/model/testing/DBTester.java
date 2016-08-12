package edu.ncrn.cornell.model.testing;
/**
 * @author Brandon Elam Barker
 * @date 8/12/2016
 */


import edu.ncrn.cornell.model.dao.FieldDao;
import edu.ncrn.cornell.model.dao.MappingDao;
import edu.ncrn.cornell.model.dao.ProfileDao;
import edu.ncrn.cornell.model.dao.SchemaDao;
import edu.ncrn.cornell.model.Field;
import edu.ncrn.cornell.model.Mapping;
import edu.ncrn.cornell.model.MappingPK;
import edu.ncrn.cornell.model.Schema;
import edu.ncrn.cornell.model.SchemaPK;
import edu.ncrn.cornell.model.testing.DBChecker;
import org.junit.gen5.api.Test;
//TODO: might need to be import org.junit.Test;

import java.util.List;
import static org.junit.gen5.api.Assertions.assertEquals;


public class DBTester extends DBChecker {

    @Test
    public void mappingsIterate() {
        List<Mapping> mappings = mappingDao.findAll();


        long nullSchemaEntries = mappings.stream().map(m -> m.getSchema() == null)
                .filter(b -> b).count();
        assertEquals(0L, nullSchemaEntries);


        long nullFieldEntries = mappings.stream().map(m -> m.getField() == null)
                .filter(b -> b).count();
        assertEquals(0L, nullFieldEntries);


    }
}
