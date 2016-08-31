package edu.ncrn.cornell.service;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.ncrn.cornell.model.Field;
import edu.ncrn.cornell.model.FieldIndice;
import edu.ncrn.cornell.model.FieldInst;
import edu.ncrn.cornell.model.Mapping;
import edu.ncrn.cornell.model.Profile;
import edu.ncrn.cornell.model.ProfileField;
import edu.ncrn.cornell.model.RawDoc;
import edu.ncrn.cornell.model.dao.FieldDao;
import edu.ncrn.cornell.model.dao.FieldIndiceDao;
import edu.ncrn.cornell.model.dao.FieldInstDao;
import edu.ncrn.cornell.model.dao.MappingDao;
import edu.ncrn.cornell.model.dao.ProfileDao;
import edu.ncrn.cornell.model.dao.ProfileFieldDao;
import edu.ncrn.cornell.model.dao.RawDocDao;
import edu.ncrn.cornell.model.dao.SchemaDao;
import edu.ncrn.cornell.model.Schema;
import edu.ncrn.cornell.model.SchemaPK;
import edu.ncrn.cornell.util.XMLHandle;
import org.springframework.stereotype.Service;

/**
 * Service class providing reusable functions for editing codebooks
 * 
 * @author kylebrumsted
 *
 */
@Service
public class CodebookEditService {
	
	@Autowired
	RawDocDao rawDocDao;
	@Autowired
	ProfileDao profileDao;
	@Autowired
	FieldDao fieldDao;
	@Autowired
	ProfileFieldDao profileFieldDao;
	@Autowired
	MappingDao mappingDao;
	@Autowired
	SchemaDao schemaDao;
	@Autowired
	FieldInstDao fieldInstDao;
	@Autowired
	FieldIndiceDao fieldIndiceDao;
	
	
	
	/**
	 * Adds a new field to a codebook titlepage. Online fields that are valid according to the schema
	 * will be allowed as additions.
	 * 
	 * Triggers SQL edits and XML edits.
	 * 
	 * @param handle : codebook to edit
	 * @param fieldId : new field to add
	 * @param newValue : value for the field
	 * @return : whether the operation was successful or not.
	 */
	public boolean addCodebookField(String handle, String fieldId, String newValue)
	{
		
		
		return false;
	}
	
	
	/**
	 * edit an existing field in a codebook (titlepage)
	 * 
	 * calls the edits for all necessary SQL tables as well as the XML rawDoc
	 * 
	 * @param handle : codebook to edit
	 * @param fieldId : field to edit
	 * @param newValue : new value for field
	 * @return whether the edit was successfull
	 */
	public boolean editCodebookField(String handle, String fieldId, String newValue)
	{
		
		
		return false;
	}
	
	/**
	 * Deletes a field in a codebook titlepage
	 * 
	 * @param handle : codebook to edit
	 * @param fieldId : field to delete
	 * @return : whether or not the operation was successful
	 */
	public boolean deleteCodebookField(String handle, String fieldId)
	{
		
		
		return false;
	}
	
	
	/**
	 * Adds a new variable to a codebook. 
	 * 
	 * Edits SQL and XML
	 * 
	 * @param handle : codebook to edit
	 * @param varname : Name of new variable
	 * @return : whether the operation was successful or not
	 */
	public boolean addVariable(String handle, String varname)
	{
		
		/*
		 *This must update fieldInst and also fieldIndices. 
		 *FieldIndices will probably be complicated.
		 */
		
		return false;
	}
	
	public boolean deleteVariable(String handle, String varname)
	{
		
		
		return false;
	}
	
	
	public boolean addVariableField(String handle, String varname, String fieldId, String newValue)
	{
		
		return false;
	}
	
	
	public boolean editVariableField(String handle, String varname, String fieldId, String newValue)
	{
		
		
		return false;
	}
	
	public boolean deleteVariableField(String handle, String varname, String fieldId)
	{
		
		return false;
	}
	
	

}
