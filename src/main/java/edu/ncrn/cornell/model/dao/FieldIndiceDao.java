package edu.ncrn.cornell.model.dao;

import java.util.List;

import edu.ncrn.cornell.model.FieldIndice;
import edu.ncrn.cornell.model.FieldIndicePK;

public interface FieldIndiceDao extends BaseRepository<FieldIndice, FieldIndicePK> {
	public List<FieldIndice> findById_FieldInstId(Long id);
}
