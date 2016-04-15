package edu.ncrn.cornell.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.ncrn.cornell.model.Field;
import edu.ncrn.cornell.model.FieldInst;
import edu.ncrn.cornell.model.Mapping;
import edu.ncrn.cornell.model.Profile;
import edu.ncrn.cornell.model.ProfileField;
import edu.ncrn.cornell.model.RawDoc;
import edu.ncrn.cornell.model.dao.FieldDao;
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
 * This class is a set of reusable function for getting structured information from postgres.
 * Info returned in forms easily parseable for display by JSP
 * 
 * Currently the profiles are hardcoded into these functions.
 * TODO: dynamic profile reading?
 * 
 * @author kylebrumsted
 *
 */
@Service
public class CodebookService {

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
	
	/**
	 * Lists all handles from the RawDocs table in postgres
	 * @return
	 */
	public List<String> getAllHandles(){
		
		List<RawDoc> docs = rawDocDao.findAll();
		System.out.println("rawdoc count: "+docs.size());
		List<String> handles = new ArrayList<String>();
		for(RawDoc doc : docs){
			handles.add(doc.getId());
		}
		
		return handles;
	}
	
	/**
	 * Returns a map of key-value pairs representing the information described by the codebookDetails profile.
	 * @param handle
	 * @return
	 */
	public Map<String, String> getCodebookDetails(String handle){
		
		List<String> field_ids = getProfileFieldIds("codebookdetails");
		
		XMLHandle xhandle = getCodebookXML(handle);
		
		
		//get each mapping, execute xpath to get value, put into hashmap
		Map<String,String> details = new HashMap<String, String>();
		for(String f : field_ids){
			//get XPath mapping associated with field
			List<Mapping> maps = mappingDao.findById_FieldId(f);
			//this is not a good check but need something here
			if(maps == null){
				System.out.println("no mappings found for "+f);
				continue;
			}
			Mapping firstMap = maps.get(0);
			String path = firstMap.getXpath();
			String value = xhandle.getXPathSingleValue(path);
			if(value == null){
				System.out.println("no value found for xpath "+path);
				continue;
			}
			Field cur = fieldDao.findOne(f);
			details.put(cur.getDisplayName(), value);
		}
		
		return details;
		
	}
	
	/**
	 * gathers codebook details from FieldInst table rather than parsing XML
	 * @param handle
	 * @return
	 */
	public Map<String, String> getCodebookDetails_SQL(String handle){
		
		//Map of field names and their corresponding instances
		Map<String, String> details = new HashMap<String, String>();
		//List of field for the codebookdetails profile
		List<String> fieldIds = getProfileFieldIds("codebookdetails");
		
		//iterate over fields, try to find corresponding instance for specified handle
		for(String f : fieldIds){
			Field curField = fieldDao.findOne(f);
			String dispName = curField.getDisplayName();
			
			List<FieldInst> fieldInsts = fieldInstDao.findByRawDocAndField1(handle, f);
			String value = "";
			
			//check for multiplicities and concatenate values accordingly
			if(fieldInsts.size() > 1){
				for(FieldInst fi : fieldInsts) value += fi.getValue() + " \n";
			}else if(fieldInsts.size() == 1){
				FieldInst fi = fieldInsts.get(0);
				value = fi.getValue();
			}else{
				System.out.println("[READING FIELDISNTS]:: No FieldInst for codebook "+handle+" field "+f);
				continue;
			}
			
			//add display name of field and instance value to the map
			details.put(dispName, value);
		}
		
		
		return details;
	}
	
	/**
	 * Gets the list of variables for a given codebook.
	 * The profile of this list is comprised of varname and varlabel.
	 * This profile is currently hardcoded into the function.
	 * TODO: generate profile dynamically
	 * 
	 * @param handle
	 * @return
	 */
	public Map<String, String> getCodebookVariables(String handle){
		//Get XPath mapping for varname and varlabel
		XMLHandle xhandle = getCodebookXML(handle);
		List<Mapping> varNameMaps = mappingDao.findById_FieldId("varname");
		List<Mapping> varLabelMaps = mappingDao.findById_FieldId("varlabel");
		Mapping varNameMap = varNameMaps.get(0);
		Mapping varLabelMap = varLabelMaps.get(0);
		String varNameXPath = varNameMap.getXpath();
		String varLabelXPath = varLabelMap.getXpath();
		
		//get list of all varnames
		List<String> vars = xhandle.getValueList(varNameXPath);
		
		//The map of name,label pairs to be returned
		Map<String, String> varlist = new HashMap<String, String>();
		
		//iterate over each varname, find associated label, add (name,label) to map
		for(String var : vars){
			String currentLabelXPath = getXPathWithVarname(varLabelXPath, var);
			String label = xhandle.getXPathSingleValue(currentLabelXPath);
			varlist.put(var, label);
		}
		
		return varlist;
	}
	
	public Map<String, String> getCodebookVariables_SQL(String handle){
		
		
		return null;
	}
	
	/**
	 * Service to retrieve the map of Fields and their values for the variable details page
	 * @param handle
	 * @param varname
	 * @return
	 */
	public Map<String, String> getVariableDetails(String handle, String varname){
		
		List<String> fieldIds = getProfileFieldIds("vardetails");
		XMLHandle xhandle = getCodebookXML(handle);
		
		Map<String, String> details = new HashMap<String, String>();
		for(String f : fieldIds){
			//XMLHandle fn to collect var details
			//get XPath mapping associated with field
			List<Mapping> maps = mappingDao.findById_FieldId(f);
			//this is not a good check but need something here
			if(maps == null){
				System.out.println("no mappings found for "+f);
				continue;
			}
			Mapping firstMap = maps.get(0);
			String path = firstMap.getXpath();
			String pathWithName = getXPathWithVarname(path, varname);
			String value = xhandle.getXPathSingleValue(pathWithName);
			if(value == null || value.equals("")){
				System.out.println("no value found for xpath "+pathWithName);
				continue;
			}
			Field cur = fieldDao.findOne(f);
			details.put(cur.getDisplayName(), value);
		}
		
		
		return details;
	}
	
	/***** Private utility functions  ******/
	
	private List<String> getProfileFieldIds(String profileId){
		List<ProfileField> proFields = profileFieldDao.findByProfileId(profileId);
		List<String> fieldIds = new ArrayList<String>();
		for(ProfileField pf : proFields){
			fieldIds.add(pf.getFieldId());
		}
		
		return fieldIds;
	}
	
	private XMLHandle getCodebookXML(String handle){
		
		RawDoc codebook = rawDocDao.findOne(handle);
		if(codebook == null) return null;
		
		String xml = codebook.getRawXml();
		Schema s = codebook.getSchema();
		SchemaPK spk = s.getId();
		String sVers = spk.getVersion();
		//System.out.println("xml: "+xml.substring(0, 150));
		String schemaURL = "";
		List<Schema> schemas = schemaDao.findById_Version(sVers);
		if(schemas.isEmpty()){
			System.out.println("no schemas founds");
		}else{
			Schema schema = schemas.get(0);
			schemaURL = schema.getUrl();
		}
		XMLHandle xhandle = new XMLHandle(xml, schemaURL);
		
		return xhandle;
	}

    private String getXPathWithVarname(String xpath, String varname){
		return xpath.replace("*", "@name='"+varname+"'");
	}
	
	
		

	
}
