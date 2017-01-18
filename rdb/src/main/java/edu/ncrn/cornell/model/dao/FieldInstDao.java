package edu.ncrn.cornell.model.dao;

import java.util.List;

import edu.ncrn.cornell.model.FieldInst;

public interface FieldInstDao extends BaseRepository<FieldInst, Long> {
	public List<FieldInst> findByRawDocIdAndFieldId(String rawDocId, String fieldId);
	public List<FieldInst> findByRawDocIdAndCanonicalXpath(String rawDocId, String canonicalXpath);
	public List<FieldInst> findByRawDocIdAndValue(String rawDocId, String value);
	public List<FieldInst> findByFieldId(String fieldId);
	public List<FieldInst> findByCanonicalXpath(String canonicalXpth);
}
