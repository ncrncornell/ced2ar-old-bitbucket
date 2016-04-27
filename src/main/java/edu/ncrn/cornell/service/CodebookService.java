package edu.ncrn.cornell.service;

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
	@Autowired
	FieldIndiceDao fieldIndiceDao;
	
	/**
	 * Lists all handles and full names
	 * @return
	 */
	public Map<String, String> getAllHandles(){
		
		Map<String, String> handles = new HashMap<String, String>();
		List<RawDoc> docs = rawDocDao.findAll();
		for(RawDoc doc : docs){
			String handle = doc.getId();
			List<FieldInst> codebookNames = fieldInstDao.findByRawDocIdAndFieldId(handle, "codebookname");
			if(codebookNames.size() != 1){
				System.out.println("unexpected result from fieldInst retreival");
				continue;
			}
			FieldInst codebookName = codebookNames.get(0);
			System.out.println("[getAllHandles]:: adding (handle, name): ("+handle+", "+codebookName.getValue());
			handles.put(handle, codebookName.getValue());
		}
		
		return handles;
	}
	
	
	/**
	 * gathers codebook details from FieldInst table rather than parsing XML
	 * @param handle
	 * @return
	 */
	public Map<String, String> getCodebookDetails(String handle){
		
		//Map of field names and their corresponding instances
		Map<String, String> details = new HashMap<String, String>();
		//List of field for the codebookdetails profile
		List<String> fieldIds = getProfileFieldIds("codebookdetails");
		
		//iterate over fields, try to find corresponding instance for specified handle
		for(String f : fieldIds){
			Field curField = fieldDao.findOne(f);
			String dispName = curField.getDisplayName();
			
			List<FieldInst> fieldInsts = fieldInstDao.findByRawDocIdAndFieldId(handle, f);
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
	 * Gets the list of variables for a given codebook (name, label) pairs
	 * The profile of this list is comprised of varname and varlabel.
	 * This profile is currently hardcoded into the function.
	 * TODO: generate profile dynamically
	 * 
	 * @param handle
	 * @return
	 */
	public Map<String, String> getCodebookVariables(String handle){
		//hashmap with varnames as keys and corresponding varlabls as values
		Map<String, String> variables = new HashMap<String, String>();
		
		//get all varname instances for a given codebook
		List<FieldInst> varnames = fieldInstDao.findByRawDocIdAndFieldId(handle, "varname");
		
		//for each varname find the labl and add to hashmap
		for( FieldInst varname : varnames){
			
			Long varnameId = varname.getId();
			List<FieldIndice> varIndices = fieldIndiceDao.findById_FieldInstId(varnameId);
			FieldIndice varIndex = varIndices.get(0);
			String varIndexValue = varIndex.getIndexValue();
			
			List<Mapping> lablMaps = mappingDao.findById_FieldId("varlabel");
			Mapping lablMap = lablMaps.get(0);
			String lablXpath = lablMap.getXpath();
			
			lablXpath = lablXpath.replace("*", varIndexValue);
			
			//find corresponding varlabl by canonical xpath
			List<FieldInst> varlabls = fieldInstDao.findByRawDocIdAndCanonicalXpath(handle, lablXpath);
			//check that xpath was mapped correctly
			if(varlabls.size() != 1){
				System.out.println("failed to properly map xpath from varname to varlabl: "+lablXpath);
				continue;
			}
			FieldInst varlabl = varlabls.get(0);
			//insert into hashmap
			variables.put(varname.getValue(), varlabl.getValue());
		}
		
		return variables;
	}
	
	/**
	 * retreives variable details profile from SQL tables
	 * @param handle
	 * @param varname
	 * @return
	 */
	public Map<String, String> getVariableDetails(String handle, String varname){
		//retreive vardetails profile
		List<String> fieldIds = getProfileFieldIds("vardetails");
		Map<String, String> details = new HashMap<String, String>();
		
		//find the varname instance specified by argument
		List<FieldInst> varnames = fieldInstDao.findByRawDocIdAndValue(handle, varname);
		if(varnames.size() != 1){
			System.out.println("failed to find variable "+varname+" in codebook "+handle);
			return null;
		}
		FieldInst var = varnames.get(0);
		
		//find the xpath index of this variable for use in retrieving other fields
		long varId = var.getId();
		List<FieldIndice> varIndices = fieldIndiceDao.findById_FieldInstId(varId);
		FieldIndice varIndex = varIndices.get(0);
		String varIndexValue = varIndex.getIndexValue();
		
		//iterate over each field in the profile and find the instance using the indexed xpath
		for(String fieldId : fieldIds){
			
			Field currentField = fieldDao.findOne(fieldId);
			
			//get the general (with wildcards) xpath for each field
			List<Mapping> maps = mappingDao.findById_FieldId(fieldId);
			if(maps.size() != 1){
				System.out.println("failed to find map for field "+fieldId);
				continue;
			}
			Mapping map = maps.get(0);
			String generalXpath = map.getXpath();
			
			//replace wildcard with index
			String indexedXpath = generalXpath.replace("*", varIndexValue);
			
			//find the instance using the indexed xpath
			List<FieldInst> insts = fieldInstDao.findByRawDocIdAndCanonicalXpath(handle, indexedXpath);
			if(insts.size() != 1){
				System.out.println("failed to find field instance with xpath "+indexedXpath);
				continue;
			}
			FieldInst inst = insts.get(0);
			
			//add to hashmap; key is display name of field and value is the text value of the FieldInst
			details.put(currentField.getDisplayName(), inst.getValue());
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
		String sVers = codebook.getSchemaVersion();
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

	
}
