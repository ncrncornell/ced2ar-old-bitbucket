package edu.ncrn.cornell.model.testing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.ncrn.cornell.model.dao.FieldDao;
import edu.ncrn.cornell.model.dao.MappingDao;
import edu.ncrn.cornell.model.dao.ProfileDao;
import edu.ncrn.cornell.model.dao.SchemaDao;
import edu.ncrn.cornell.model.Field;
import edu.ncrn.cornell.model.Mapping;
import edu.ncrn.cornell.model.MappingPK;
import edu.ncrn.cornell.model.Schema;
import edu.ncrn.cornell.model.SchemaPK;

/**
 * Class checks the health of the database
 * and fill the structural tables if necessary
 * 
 * @author kylebrumsted
 *
 */
@Component
public class DBChecker {


	@Autowired
	public FieldDao fieldDao;
	@Autowired
	public MappingDao mappingDao;
	@Autowired
	public SchemaDao schemaDao;
	@Autowired
	public ProfileDao profileDao;

	public ArrayList<String> fields;


	public DBChecker() {
		
		/*
		 * Probably all of these will be needed elsewhere.
		 * TODO: Should eventually move all of this structural info into a config file of some sort.
		 * or, remove all this fn'ality and put it into an SQL script to be executed on startup
		 */
		fields = new ArrayList<String>();
		//variable fields
		fields.add("varname");
		fields.add("varvalue");
		fields.add("varlabel");
		fields.add("varaccess");
		fields.add("vartype");
		fields.add("varfiles");
		fields.add("vardesc");
		fields.add("sumstat");
		fields.add("valrange");
		fields.add("valrangemin");
		fields.add("valrangemax");
		//codebook fields
		fields.add("codebookname");
		fields.add("codebookalt");
		fields.add("codebookdist");
		fields.add("codebookcit");
		fields.add("datacit");
		fields.add("abstract");
		fields.add("relatedmaterial");

	}

	/**
	 * top level public method for checking that all necessary structural info is in postgres
	 */
	public boolean DBinitIsOk() {
		fieldsInit();
		schemasInit();
		//mappings is broken. PK changed, need to figure how to search by field
		mappingsInit();

		return true;
	}

	/**
	 * method to check fields table in postgres
	 * iterates over all fields that should exist and checks if they're there
	 * passes all missing fields to creation function
	 */
	private void fieldsInit() {

		ArrayList<String> missingFields = (ArrayList<String>) fields.clone();
		int fsize = missingFields.size();

		//iterate over fields, removing ones that exist in postgres
		for (Iterator<String> it = missingFields.iterator(); it.hasNext(); ) {
			String field_id = it.next();
			try {
				Field f = fieldDao.findOne(field_id);
				if (f != null) it.remove();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		int missingFsize = missingFields.size();
		System.out.println("[DBCHECKER]:: missing " + missingFsize + "/" + fsize + " fields");
		//if some fields are absent, create them.
		if (missingFsize > 0) fillFields(missingFields);


	}

	/**
	 * TODO: create service classes for these types of methods?
	 *
	 * @param id       : Field ID
	 * @param dispName : Display name of field
	 * @param desc     : Description of field
	 */
	private void createField(String id, String dispName, String desc) {
		String fieldId = "";
		try {
			Field field = new Field(id);
			field.setDisplayName(dispName);
			field.setDescription(desc);
			fieldDao.save(field);
			fieldId = String.valueOf(field.getId());
		} catch (Exception e) {
			System.out.println("[DBCHECKER]:: Error creating default field " + id);
			e.printStackTrace();
		}
		System.out.println("[DBCHECKER]:: Successfully created field with ID " + fieldId);
	}

	/**
	 * private method for inserting the predefined fields into postgres.
	 * TODO:This seems bad due to a lot of hard coding. Perhaps I should run an SQL script instead?
	 *
	 * @param missingFields
	 */
	private void fillFields(List<String> missingFields) {

		//VARVALUE
		if (missingFields.contains("varvalue")) {
			createField("varvalue", "Variable Value", "The value of a variables");
			//check for/add mappings?
		}

		//VARNAME
		if (missingFields.contains("varname")) {
			createField("varname", "Variable Name", "The name of a variable");
		}

		//VARLABEL
		if (missingFields.contains("varlabel")) {
			createField("varlabel", "Label", "The label of a variable");
		}

		//VARACCESS
		if (missingFields.contains("varaccess")) {
			createField("varaccess", "Top Access Level", "The top access level of a variable");
		}

		//VARTYPE
		if (missingFields.contains("vartype")) {
			createField("vartype", "Type", "The type of a variable");
		}

		//VARFILES
		if (missingFields.contains("varfiles")) {
			createField("varfiles", "Files", "The files associated with a variable");
		}

		//VARDESC
		if (missingFields.contains("vardesc")) {
			createField("vardesc", "Description", "The description of a variable");
		}

		//SUMSTAT
		if (missingFields.contains("sumstat")) {
			createField("sumstat", "Summary Statistics", "Statistical measures describing responses to a variable");
		}

		//VALRANGE
		if (missingFields.contains("valrange")) {
			createField("valrange", "Value Range", "Range of possible values for responses to a variable");
		}

		//VALRANGEMIN
		if (missingFields.contains("valrangemin")) {
			createField("valrangemin", "Minimum", "Minimum possible value in the value range");
		}

		//VALRANGEMAX
		if (missingFields.contains("valrangemax")) {
			createField("valrangemax", "Maximum", "Maximum possible value in the value range");
		}

		//CODEBOOKNAME
		if (missingFields.contains("codebookname")) {
			createField("codebookname", "Codebook Name", "Name of a codebook");
		}

		//CODEBOOKALT
		if (missingFields.contains("codebookalt")) {
			createField("codebookalt", "Alternate Title", "Alternate title for a codebook");
		}

		//CODEBOOKDIST
		if (missingFields.contains("codebookdist")) {
			createField("codebookdist", "Distributor Statement", "Distribution statement for the work of a codebook");
		}

		//CODEBOOKCIT
		if (missingFields.contains("codebookcit")) {
			createField("codebookcit", "Codebook Citation", "Bibliographic information for a codebook");
		}

		//DATACIT
		if (missingFields.contains("datacit")) {
			createField("datacit", "Dataset Citation", "Bibliographic information for a dataset");
		}

		//ABSTRACT
		if (missingFields.contains("abstract")) {
			createField("abstract", "Abstract", "Abstract for a codebook");
		}


		//RELATEDMATERIAL
		if (missingFields.contains("relatedmaterial")) {
			createField("relatedmaterial", "Related Material", "Supplemental material related to a codebook");
		}


	}

	/**
	 * Checks for default Schema in postgres
	 */
	private void schemasInit() {
		SchemaPK spk = new SchemaPK();
		spk.setId("ddi");
		spk.setVersion("2.5.1");
		Schema schema = schemaDao.findOne(spk);
		if (schema == null) {
			createSchema(spk, "http://www.ddialliance.org/Specification/DDI-Codebook/2.5/XMLSchema/codebook.xsd");
			System.out.println("Tried creating ddi:2.5.1");
		}

		spk = new SchemaPK();
		spk.setId("lifecycle");
		spk.setVersion("3.2");
		schema = schemaDao.findOne(spk);
		if (schema == null) {
			createSchema(spk, "http://www.ddialliance.org/Specification/DDI-Lifecycle/3.2/XMLSchema/instance.xsd");
			System.out.println("Tried creating lifecycle:3.2");
		}
	}


	/**
	 * Saves a schema in postgres
	 * @param schemaPK
	 * @param url
	 */
	private void createSchema(SchemaPK schemaPK, String url) {
		try {
			Schema schema = new Schema();
			schema.setId(schemaPK);
			schema.setUrl(url);
			schemaDao.save(schema);
		} catch (Exception e) {
			System.out.println("[DBCHECKER]:: failed to create default schema in postgres");
			e.printStackTrace();
		}
		System.out.println("[DBCHECKER]:: successfully created default schema");
	}

	/**
	 * method to check the mappings table.
	 * Checks to see if mappings exist for each of the fields listed.
	 * Passes all missing mappings to creation function
	 */
	private void mappingsInit() {
		ArrayList<String> missingMappings = (ArrayList<String>) fields.clone();
		int msize = missingMappings.size();

		//iterate over fields, removing ones that have mappings in postgres
		for (Iterator<String> it = missingMappings.iterator(); it.hasNext(); ) {
			String field_id = it.next();
			try {
				ArrayList<Mapping> m = (ArrayList<Mapping>) mappingDao.findById_FieldId(field_id);
				if (m.size() > 0) it.remove();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		int missingMsize = missingMappings.size();
		System.out.println("[DBCHECKER]:: fields with missing mappings: "
				+ missingMsize + "/" + msize + " mappings"
		);
		//if some fields are absent, create them.
		if (missingMsize > 0) fillMappings();
	}

	private void createMapping(MappingPK mpk, String xpath) {
		try {
			//create mapping model
			Mapping mapping = new Mapping();
			mapping.setId(mpk);
			mapping.setXpath(xpath);
			mappingDao.save(mapping);
		} catch (Exception e) {
			System.out.println("[DBCHECKER]:: error creating mapping of field "
					+ mpk.getFieldId());
			e.printStackTrace();
		}
		System.out.println("[DBCHECKER]:: successfully created mapping for "
				+ mpk.getFieldId());

	}

	/**
	 * iterates over the list of missing mappings and adds a single mapping for each missing field
	 * TODO: needs to be more robust, should be multiple mappings for each field.
	 */
	private void fillMappings() {
		//VARVALUE
//		if(missingMappings.contains("varvalue")){
//			//TODO:valrng is wrong? unclear
//			//createMapping("varvalue","/codebook/dataDscr/var[*]/valrng");
//		}

		//VARNAME
		MappingPK mpk = makeMappingPK("ddi", "2.5.1", "varname");
		Mapping mapping = mappingDao.findOne(mpk);
		if (mapping == null) {
			createMapping(mpk, "/codeBook/dataDscr/var[*]/@name");
		}

		//VARLABEL
		mpk = makeMappingPK("ddi", "2.5.1", "varlabel");
		mapping = mappingDao.findOne(mpk);
		if (mapping == null) {
			createMapping(mpk, "/codeBook/dataDscr/var[*]/labl");
		}

		//VARACCESS
//		if(missingMappings.contains("varaccess")){
//			//TODO:not sure where access level is
//			//createMapping("varaccess","/codebook/dataDscr/var[*]/??")
//		}

		//VARTYPE
		mpk = makeMappingPK("ddi", "2.5.1", "vartype");
		mapping = mappingDao.findOne(mpk);
		if (mapping == null) {
			createMapping(mpk, "/codeBook/dataDscr/var[*]/varFormat/@type");
		}

		//VARFILES
		mpk = makeMappingPK("ddi", "2.5.1", "varfiles");
		mapping = mappingDao.findOne(mpk);
		if (mapping == null) {
			createMapping(mpk, "/codeBook/fileDscr[*]");
		}

		//VARDESC
		mpk = makeMappingPK("ddi", "2.5.1", "vardesc");
		mapping = mappingDao.findOne(mpk);
		if (mapping == null) {
			createMapping(mpk, "/codeBook/dataDscr/var[*]/txt");
		}

		//SUMSTAT
		mpk = makeMappingPK("ddi", "2.5.1", "sumstat");
		mapping = mappingDao.findOne(mpk);
		if (mapping == null) {
			createMapping(mpk, "/codeBook/dataDscr/var[*]/sumstat[*]");
		}

		//VALRANGE
		mpk = makeMappingPK("ddi", "2.5.1", "valrange");
		mapping = mappingDao.findOne(mpk);
		if (mapping == null) {
			createMapping(mpk, "/codeBook/dataDscr/var[*]/valrng");
		}

		//VALRANGEMIN
		mpk = makeMappingPK("ddi", "2.5.1", "valrangemin");
		mapping = mappingDao.findOne(mpk);
		if (mapping == null) {
			createMapping(mpk, "/codeBook/dataDscr/var[*]/valrng/@min");
		}

		//VALRANGEMAX
		mpk = makeMappingPK("ddi", "2.5.1", "valrangemax");
		mapping = mappingDao.findOne(mpk);
		if (mapping == null) {
			createMapping(mpk, "/codeBook/dataDscr/var[*]/valrng/@max");
		}

		//CODEBOOKNAME
		mpk = makeMappingPK("ddi", "2.5.1", "codebookname");
		mapping = mappingDao.findOne(mpk);
		if (mapping == null) {
			createMapping(mpk, "/codeBook/docDscr/citation/titlStmt/titl");
		}

		//CODEBOOKALT
		mpk = makeMappingPK("ddi", "2.5.1", "codebookalt");
		mapping = mappingDao.findOne(mpk);
		if (mapping == null) {
			createMapping(mpk, "/codeBook/docDscr/citation/titlStmt/altTitl");
		}

		//CODEBOOKDIST
		mpk = makeMappingPK("ddi", "2.5.1", "codebookdist");
		mapping = mappingDao.findOne(mpk);
		if (mapping == null) {
			createMapping(mpk, "/codeBook/docDscr/citation/distStmt/distrbtr[*]");
		}

		//CODEBOOKCIT
		mpk = makeMappingPK("ddi", "2.5.1", "codebookcit");
		mapping = mappingDao.findOne(mpk);
		if (mapping == null) {
			createMapping(mpk, "/codeBook/docDscr/citation/biblCit");
		}
		mpk = makeMappingPK("lifecycle", "3.2", "codebookcit");
		mapping = mappingDao.findOne(mpk);
		if (mapping == null) {
			//Note: xpath untested for validity
			createMapping(mpk, "/FragmentInstance/Fragment/dDDIInstance/[name='Citation']");
		}

		//DATACIT
		mpk = makeMappingPK("ddi", "2.5.1", "datacit");
		mapping = mappingDao.findOne(mpk);
		if (mapping == null) {
			createMapping(mpk, "/codeBook/stdyDscr/citation/biblCit");
		}
		mpk = makeMappingPK("lifecycle", "3.2", "datacit");
		mapping = mappingDao.findOne(mpk);
		if (mapping == null) {
			//Note: xpath untested for validity
			createMapping(mpk, "/FragmentInstance/Fragment/dDDIInstance/[name='Citation']");
		}

		//ABSTRACT
		mpk = makeMappingPK("ddi", "2.5.1", "abstract");
		mapping = mappingDao.findOne(mpk);
		if (mapping == null) {
			createMapping(mpk, "/codeBook/stdyDscr/stdyInfo/abstract");
		}

		//RELATEDMATERIAL
		mpk = makeMappingPK("ddi", "2.5.1", "relatedmaterial");
		mapping = mappingDao.findOne(mpk);
		if (mapping == null) {
			createMapping(mpk, "/codeBook/stdyDscr/othrStdMat/relMat[*]");
		}
	}

	private MappingPK makeMappingPK(String schemaId, String schemaVersion, String fieldId) {
		MappingPK mappingPK = new MappingPK();
		mappingPK.setFieldId(fieldId);
		mappingPK.setSchemaId(schemaId);
		mappingPK.setSchemaVersion(schemaVersion);
		return mappingPK;
	}

	private void profilesInit() {

	}

}
