package edu.ncrn.cornell.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.ncrn.cornell.model.dao.FieldDao;

/**
 * Class check the health of the database
 * and fill the structural tables if necessary
 * 
 * @author kylebrumsted
 *
 */
@Component
public class DBChecker {
	
	
	@Autowired
	private FieldDao fieldDao;
	
	
	public DBChecker(){
	}
	
	public void DBinit(){
		checkFields();
	}
	
	private void checkFields(){
		long fieldCount = fieldDao.count();
		if(fieldCount > 0){
			System.out.println("[DBCHECKER]:: Field table contains records");
		}else{
			System.out.println("[DBCHECKER]:: Field table doesn't contain records");
		}
		
	}

	
	
	
	
}
