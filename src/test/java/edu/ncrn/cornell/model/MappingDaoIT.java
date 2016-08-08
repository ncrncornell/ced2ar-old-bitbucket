package edu.ncrn.cornell.model;

import edu.ncrn.cornell.model.*;
import org.junit.Test;

import java.util.List;

import static org.junit.gen5.api.Assertions.assertEquals;

/**
 * Created by Brandon Barker on 8/8/2016.
 */


public class MappingDaoIT extends BaseRepositoryIT {

    @Test
    public void mappingsIterate() {
        List<Mapping> mappings = dbChecker.mappingDao.findAll();


        long nullSchemaEntries = mappings.stream().map(m -> m.getSchema() == null)
                .filter(b -> b).count();
        assertEquals(0L, nullSchemaEntries);


        long nullFieldEntries = mappings.stream().map(m -> m.getField() == null)
            .filter(b -> b).count();
        assertEquals(0L, nullFieldEntries);


    }



}
