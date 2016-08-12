package edu.ncrn.cornell.model.dao;


import java.util.List;

import edu.ncrn.cornell.model.Schema;
import edu.ncrn.cornell.model.SchemaPK;

public interface SchemaDao extends BaseRepository<Schema, SchemaPK> {
	public List<Schema> findById_Version(String version);
}
