package edu.ncrn.cornell.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;

import edu.ncrn.cornell.model.Schema;
import edu.ncrn.cornell.model.Field;
import edu.ncrn.cornell.model.dao.FieldDao;
import edu.ncrn.cornell.model.dao.FieldIndicyDao;
import edu.ncrn.cornell.model.dao.FieldInstDao;
import edu.ncrn.cornell.model.dao.MappingDao;
import edu.ncrn.cornell.model.dao.SchemaDao;
import edu.ncrn.cornell.util.XMLHandle;


/**
 * Class to handle the upload mechanisms of new codebooks:
 * Upon upload, cascade new data into the field_inst and field indices tables
 * Also check for new schemas.
 * @author kylebrumsted
 *
 */
public class UploadService {
	
	//Autowired DAOs
	@Autowired
	private FieldInstDao fieldInstDao;
	@Autowired
	private FieldIndicyDao fieldIndicyDao;
	@Autowired
	private FieldDao fieldDao;
	@Autowired
	private SchemaDao schemaDao;
	@Autowired
	private MappingDao mappingDao;
	
	//Other private members
	private static final String DEFAULT_SCHEMA_VERSION = "2.5";
	
	/**
	 * hook function to be called by UploadController for a new XML codebook
	 * @param f
	 * @return
	 */
	public boolean newUpload(File f){
		//Convert file to string
		String xml = "";
		try{
			xml = FileUtils.readFileToString(f);
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		//Validate
		//TODO: clean namespaces for new documents
		List<Schema> schemas = schemaDao.findById_Version(DEFAULT_SCHEMA_VERSION);
		Schema schema = schemas.get(0);
		String schemaURL = schema.getUrl();
		XMLHandle xhandle = new XMLHandle(xml, schemaURL);
		if(!xhandle.isValid()) return false;
		
		//if file is valid, parse the rawdoc into fields
		return updateFieldInsts(xhandle);
	}
	
	/**
	 * fills SQL tables from XML codebook
	 * @param xhdl
	 * @return
	 */
	private boolean updateFieldInsts(XMLHandle xhdl){
		List<Field> fields = fieldDao.findAll();
		/*
		 * iterate over fields
		 * get mapping for each one
		 * eval xpath on codebook and get value(s) at each xpath location
		 * insert into fieldInst table
		 * convert xpath to refer to specific value
		 * 	  (i.e. /codeBook/dataDsc/var/varlabl -> /codeBook/dataDscr/var[@name='age']/labl
		 * insert as index into fieldIndicy table
		 * 
		 */
		
		
		return false;
	}
	
	
}
