package edu.ncrn.cornell.model.dao;


import edu.ncrn.cornell.model.Field;
import javax.transaction.Transactional;

@Transactional
public interface FieldDao extends BaseRepository<Field, String> {
	
}
