package edu.ncrn.cornell.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.hash.Hashing;
import edu.ncrn.cornell.model.*;
import edu.ncrn.cornell.model.dao.*;
import edu.ncrn.cornell.util.DDIHandle;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;

import edu.ncrn.cornell.util.XMLHandle;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scala.Tuple2;


/**
 * Class to handle the upload mechanisms of new codebooks:
 * Upon upload, cascade new data into the field_inst and field indices tables
 * Also check for new schemas.
 * @author Brandon Barker
 *
 */

@Service
public class UploadService {
	
	//Autowired DAOs
	@Autowired
	private FieldInstDao fieldInstDao;
	@Autowired
	private FieldIndiceDao fieldIndiceDao;
	@Autowired
	private FieldDao fieldDao;
    @Autowired
    private RawDocDao rawDocDao;
	@Autowired
	private SchemaDao schemaDao;
	@Autowired
	private MappingDao mappingDao;
	
	//Other private members
	private static final String DEFAULT_SCHEMA_VERSION = "2.5";
	private static final String DEFAULT_SCHEMA_ID = "ddi";

    private Optional<Boolean> uploadIsValid = Optional.empty();

    public Boolean uploadIsValid() {
        return uploadIsValid.orElse(false);
    }

    private Optional<Boolean> importSucceded = Optional.empty();

    public Boolean importSucceded() {
        return importSucceded.orElse(false);
    }


    /**
	 * hook function to be called by UploadController for a new XML codebook
	 * @param f
	 * @return
	 */
	public void newUpload(File f){
		//Convert file to string
		String xmlString = "";
		try{
			xmlString = FileUtils.readFileToString(f);
		}catch (IOException e) {
			//TODO: return to controller with message.
			e.printStackTrace();
		}
		
		//Validate
		//TODO: clean namespaces for new documents
		List<Schema> schemas = schemaDao.findById_Version(DEFAULT_SCHEMA_VERSION);
		Schema schema = schemas.get(0);
		String schemaURL = schema.getUrl();
		DDIHandle xhandle = new DDIHandle(xmlString, schemaURL);
		if(!xhandle.isValid()) {
            uploadIsValid = Optional.of(false);
            //TODO: delete file and redirect with error message
            return;
        }

        //if file is valid, proceed:

        // Note, we use a hash value of the input xml rather than normalized xml;
        // This is meant to be a simple check rather than a rigorous constraint.
        // TODO: Although, we may want this to do some form of normalization since,
        // TODO: expecially considering a raw_doc may be converted to another schema
        final String xmlHash = Hashing.sha256()
            .hashString(xmlString, StandardCharsets.UTF_8).toString();


        System.out.println("xmlHash is " + xmlHash);


        // persist rawdoc in database, so that we have it on record
        // TODO: Should grab some of these as default values and present a way
        // TODO: edit them in the view
        RawDoc newRawDoc = new RawDoc();
        String rawDocId = f.getName().replace(".xml", "");
        newRawDoc.setId(rawDocId);
        newRawDoc.setCodebookId(rawDocId + "_codebook");
        newRawDoc.setRawXml(xmlString);
        newRawDoc.setSchemaId(DEFAULT_SCHEMA_ID);
        newRawDoc.setSchemaVersion(DEFAULT_SCHEMA_VERSION);
        newRawDoc.setSha256(xmlHash);
        rawDocDao.save(newRawDoc);

		// parse the rawdoc into fields
		importSucceded = Optional.of(updateFieldInsts(xhandle, newRawDoc));

	}
	
	/**
	 * fills SQL tables from XML codebook
	 * @param xhandle
	 * @return
	 */
	@Transactional
	private boolean updateFieldInsts(XMLHandle xhandle, RawDoc raw_doc){
		List<Field> fields = fieldDao.findAll();
        Map<String, Field> fieldMap = new HashMap<>(fields.size());
        for (Field field: fields) {
            fieldMap.put(field.getId(), field);
        }

		/*
		 * iterate over fields
		 * get mapping for each one
		 * eval xpath on codebook and get value(s) at each xpath location
		 * insert into fieldInst table
		 * convert xpath to refer to specific value
		 * 	  (i.e. /codeBook/dataDsc/var/varlabl -> /codeBook/dataDscr/var[@name='age']/labl
		 * insert as index into fieldIndice table
		 * 
		 */

        List<String> fieldIds = fields.stream().map(Field::getId)
            .collect(Collectors.toList());

        Map<String, List<String>> fieldMappings = new HashMap<>();
        for(String fieldId: fieldIds) {
            List<String> fieldXpaths = mappingDao.findById_FieldId(fieldId).stream()
                .map(Mapping::getXpath).collect(Collectors.toList());
            fieldMappings.put(fieldId, fieldXpaths);
        }

        fieldMappings.forEach((fieldId, xpathList) -> {
            System.out.println("Xpaths for field " + fieldId + " are:");
            System.out.println(Joiner.on("\n").join(xpathList));
            System.out.println("Values are: ");

			List<Tuple2<String, List<String>>> uniqueXpaths = xpathList.stream().flatMap(xpath ->
					xhandle.getUniqueXPaths("", xpath, Collections.emptyList())
			).collect(Collectors.toList());

			List<String> xpathValues = uniqueXpaths.stream().map(xpath ->
                xhandle.getUniqueValue(xpath._1).orElse("")
			).collect(Collectors.toList());

            List<FieldInst> fieldInsts =
			IntStream.range(0, xpathValues.size()).mapToObj(ii -> {
                FieldInst fieldInst = new FieldInst();
                fieldInst.setValue(xpathValues.get(ii));
                fieldInst.setFieldId(fieldId);
                fieldInst.setRawDocId(raw_doc.getId());
				fieldInst.setCanonicalXpath(uniqueXpaths.get(ii)._1);

                return fieldInst;
            }).collect(Collectors.toList());

            //DEBUG
			System.out.println("uniqueXpaths count is " + uniqueXpaths.size());
			System.out.println("xpathValues count is " + xpathValues.size());


            // entities are altered upon insertion into database; retrive updates
            fieldInsts = Lists.newArrayList(fieldInstDao.save(fieldInsts));
            final List<FieldInst> fieldInstsTmp1 = fieldInsts;

            List<FieldIndice> allFieldIndices = IntStream.range(0, xpathValues.size()).mapToObj(ii -> {
                FieldInst fieldInst = fieldInstsTmp1.get(ii);
                List<String> xpathIndices = uniqueXpaths.get(ii)._2;

                //DEBUG
                System.out.println("xpathIndices:");
                System.out.println(xpathIndices);

                return IntStream.range(0, xpathIndices.size()).mapToObj(jj -> {
                    FieldIndice fieldIndex = new FieldIndice();
                    FieldIndicePK fieldIndexPk = new FieldIndicePK();
                    String xpathIndex = xpathIndices.get(jj);
                    //
                    fieldIndexPk.setFieldInstId(fieldInst.getId());
                    fieldIndexPk.setIndex((long) jj);
                    //
                    fieldIndex.setId(fieldIndexPk);
                    fieldIndex.setIndexValue(xpathIndex);
                    fieldIndex.setFieldInst(fieldInst);
                    return fieldIndex;
                });
            })
            .flatMap(f -> f)
            //.filter(f -> f.getIndexValue() == null)
            .collect(Collectors.toList());

            System.out.println("There are " + Integer.toString(allFieldIndices.size()) + " indicies");

            fieldIndiceDao.save(allFieldIndices);

        });


		return false;
	}
	
	
}
