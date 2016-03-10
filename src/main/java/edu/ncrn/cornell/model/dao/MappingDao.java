package edu.ncrn.cornell.model.dao;

import java.util.List;

import edu.ncrn.cornell.model.Mapping;
import edu.ncrn.cornell.model.MappingPK;

public interface MappingDao extends BaseRepository<Mapping, MappingPK> {
	
	List<Mapping> findByField_id(String field_id);
}
