package edu.ncrn.cornell.model.dao;

import java.util.List;

import edu.ncrn.cornell.model.FieldInst;

public interface FieldInstDao extends BaseRepository<FieldInst, Long> {
	public List<FieldInst> findByRawDocAndField1(String rawDoc, String field1);
}
