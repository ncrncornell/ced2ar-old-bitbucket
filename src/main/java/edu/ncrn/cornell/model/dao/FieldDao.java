package edu.ncrn.cornell.model.dao;

import org.springframework.data.repository.CrudRepository;
import edu.ncrn.cornell.model.Field;

import java.util.List;

import javax.transaction.Transactional;

@Transactional
public interface FieldDao extends CrudRepository<Field, String> {
	
	public Field findByDisplayName(String displayName);
	List<Field> findAll();

}
