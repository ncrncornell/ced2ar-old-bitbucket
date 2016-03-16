package edu.ncrn.cornell.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.ncrn.cornell.model.Field;
import edu.ncrn.cornell.model.Mapping;
import edu.ncrn.cornell.model.Profile;
import edu.ncrn.cornell.model.ProfileField;
import edu.ncrn.cornell.model.RawDoc;
import edu.ncrn.cornell.model.dao.FieldDao;
import edu.ncrn.cornell.model.dao.MappingDao;
import edu.ncrn.cornell.model.dao.ProfileDao;
import edu.ncrn.cornell.model.dao.ProfileFieldDao;
import edu.ncrn.cornell.model.dao.RawDocDao;
import edu.ncrn.cornell.util.XMLHandle;

/**
 * This class is a set of reusable function for getting structured information from postgres.
 * Info returned in forms easily parseable for display by JSP
 * @author kylebrumsted
 *
 */
@Component
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
		
		//can't figure out why this doesn't retreive properly
		//List<ProfileField> proFields = profileFieldDao.findByProfile_Id("codebookdetails");
		
		List<ProfileField> proFields = profileFieldDao.findByProfileId("codebookdetails");
		System.out.println("profileFields count: "+proFields.size());
		//Get list of fields for variable details
		List<String> field_ids = new ArrayList<String>();
		for(ProfileField pf : proFields){
			field_ids.add(pf.getFieldId());
		}
		
		//get the xml, put into DOM
		RawDoc codebook = rawDocDao.findOne(handle);
		String xml = codebook.getRawXml();
		System.out.println("xml: "+xml.substring(0, 150));
		XMLHandle xhandle = new XMLHandle(xml);
		
		
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
		

	
}
