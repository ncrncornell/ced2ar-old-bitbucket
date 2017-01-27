package edu.ncrn.cornell.service

import org.springframework.beans.factory.annotation.Autowired
import edu.ncrn.cornell.model.Field
import edu.ncrn.cornell.model.FieldIndice
import edu.ncrn.cornell.model.FieldInst
import edu.ncrn.cornell.model.Mapping
import edu.ncrn.cornell.model.ProfileField
import edu.ncrn.cornell.model.RawDoc
import edu.ncrn.cornell.model.dao.FieldDao
import edu.ncrn.cornell.model.dao.FieldIndiceDao
import edu.ncrn.cornell.model.dao.FieldInstDao
import edu.ncrn.cornell.model.dao.MappingDao
import edu.ncrn.cornell.model.dao.ProfileDao
import edu.ncrn.cornell.model.dao.ProfileFieldDao
import edu.ncrn.cornell.model.dao.RawDocDao
import edu.ncrn.cornell.model.dao.SchemaDao
import org.springframework.stereotype.Service

import scala.collection.mutable

import collection.JavaConverters._

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
class CodebookService {
  @Autowired private[service] val rawDocDao: RawDocDao = null
  @Autowired private[service] val profileDao: ProfileDao = null
  @Autowired private[service] val fieldDao: FieldDao = null
  @Autowired private[service] val profileFieldDao: ProfileFieldDao = null
  @Autowired private[service] val mappingDao: MappingDao = null
  @Autowired private[service] val schemaDao: SchemaDao = null
  @Autowired private[service] val fieldInstDao: FieldInstDao = null
  @Autowired private[service] val fieldIndiceDao: FieldIndiceDao = null

  /**
    * Lists all handles and full names
    *
    * @return
    */
  def getAllHandles: Map[String, String] = {
    val handles: mutable.Map[String, String] = mutable.Map[String, String]()
    val docs: List[RawDoc] = rawDocDao.findAll().asScala.toList
    for (doc <- docs) {
      val handle: String = doc.getId
      val codebookNames: List[FieldInst] =
        fieldInstDao.findByRawDocIdAndFieldId(handle, "codebookname").asScala.toList
      if (codebookNames.size != 1) {
        println("unexpected result from fieldInst retreival")
      }
      else {
        codebookNames.headOption match {
          case Some(codebookName) =>
            println("[getAllHandles]:: adding (handle, name): (" + handle + ", " + codebookName.getValue)
            handles.put(handle, codebookName.getValue)
          case None => ()
        }

      }
    }
    handles.toMap
  }

  /**
    * gathers codebook details from FieldInst table rather than parsing XML
    *
    * @param handle
    * @return
    */
  def getCodebookDetails(handle: String): Map[(String, Int), String] = {
    println("[getCodebookDetails]:: RETREIVING DETAILS FOR CODEBOOK " + handle)
    //Map of field names and their corresponding instances
    val details: mutable.Map[(String, Int), String] = mutable.Map()
    //List of field for the codebookdetails profile
    val fieldIds: List[String] = getProfileFieldIds("codebookdetails")
    //iterate over fields, try to find corresponding instance for specified handle
    for (fieldId <- fieldIds) {
      //get ordering for display
      val ordering: Int = getOrdering("codebookdetails", fieldId)
      val curField: Field = fieldDao.findOne(fieldId)
      val dispName: String = curField.getDisplayName.trim
      val fieldInsts: List[FieldInst] = fieldInstDao.findByRawDocIdAndFieldId(handle, fieldId)
        .asScala.toList
      var value: String = ""
      //check for multiplicities and concatenate values accordingly
      if (fieldInsts.nonEmpty) {
        for (fi <- fieldInsts) value += fi + " \n"
        //create key as tuple of field display name and ordering
        val key: (String, Int) = (dispName, ordering)
        //add tuple key and instance value to the map
        details(key) = value
      }
      else {
        println("[READING FIELDISNTS]:: No FieldInst for codebook " + handle + " field " + fieldId)
      }
    }
    details.toMap
  }

  /**
    * Retrieves all variables in the database
    *
    * @return a map of (name,label) pairs
    */
  def getAllVariables: Map[String, (String, String)] = {
    val handlesMap: Map[String, String] = getAllHandles
    val handles: List[String] = handlesMap.keySet.toList
    getVarList(handles)
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
  def getCodebookVariables(handle: String): Map[String, String] = {
    //hashmap with varnames as keys and corresponding varlabls as values
    val variables: mutable.Map[String, String] = mutable.Map()
    //get all varname instances for a given codebook
    val varnames: List[FieldInst] = fieldInstDao.findByRawDocIdAndFieldId(handle, "varname")
      .asScala.toList
    //for each varname find the labl and add to hashmap
    for (varname <- varnames) {
      val varnameId: Long = varname.getId
      val varIndices: List[FieldIndice] = fieldIndiceDao.findById_FieldInstId(varnameId).asScala.toList
      val varIndex: FieldIndice = varIndices.head
      val varIndexValue: String = varIndex.getIndexValue
      val lablMaps: List[Mapping] = mappingDao.findById_FieldId("varlabel").asScala.toList
      val lablMap: Mapping = lablMaps.head
      var lablXpath: String = lablMap.getXpath
      lablXpath = lablXpath.replace("*", varIndexValue)
      //find corresponding varlabl by canonical xpath
      val varlabls: List[FieldInst] = fieldInstDao.findByRawDocIdAndCanonicalXpath(handle, lablXpath)
        .asScala.toList
      //check that xpath was mapped correctly
      if (varlabls.size != 1) {
        println("failed to properly map xpath from varname to varlabl: " + lablXpath)
      }
      else {
        val varlabl: FieldInst = varlabls.head
        //insert into hashmap
        variables.put(varname.getValue, varlabl.getValue)
      }
    }
    variables.toMap
  }

  private def getVarList(handles: List[String]): Map[String, (String, String)] = {
    val variables: mutable.Map[String, (String, String)] = mutable.Map()
    val varnames: List[FieldInst] = fieldInstDao.findByFieldId("varname").asScala.toList
    //for each varname find the labl and add to hashmap
    for (varname <- varnames) {
      val handle: String = varname.getRawDocId
      if (handles.contains(handle)) {
        val varnameId: Long = varname.getId
        val varIndices: List[FieldIndice] = fieldIndiceDao.findById_FieldInstId(varnameId)
          .asScala.toList
        val varIndex: FieldIndice = varIndices.head
        val varIndexValue: String = varIndex.getIndexValue
        val lablMaps: List[Mapping] = mappingDao.findById_FieldId("varlabel").asScala.toList
        val lablMap: Mapping = lablMaps.head
        var lablXpath: String = lablMap.getXpath
        lablXpath = lablXpath.replace("*", varIndexValue)
        //find corresponding varlabl by canonical xpath
        val varlabls: List[FieldInst] =
          fieldInstDao.findByRawDocIdAndCanonicalXpath(handle, lablXpath).asScala.toList
        //check that xpath was mapped correctly
        if (varlabls.size != 1) {
          println("failed to properly map xpath from varname to varlabl: " + lablXpath)
        }
        else {
          val varlabl: FieldInst = varlabls.head
          //insert into hashmap
          val value: (String, String) = (varlabl.getValue, handle)
          variables.put(varname.getValue, value)
        }
      }
    }
    variables.toMap
  }

  /**
    * retrieves variable details profile from SQL tables
    *
    * @param handle
    * @param varname
    * @return
    */
  def getVariableDetails(handle: String, varname: String): Map[(String, Int), String] = {
    //retreive vardetails profile
    val fieldIds: List[String] = getProfileFieldIds("vardetails")
    val details: mutable.Map[(String, Int), String] = mutable.Map()
    //find the varname instance specified by argument
    val varnames: List[FieldInst] = fieldInstDao.findByRawDocIdAndValue(handle, varname)
      .asScala.toList
    if (varnames.size != 1) {
      println("failed to find variable " + varname + " in codebook " + handle)
      return null
    }
    val variable: FieldInst = varnames.head
    //find the xpath index of this variable for use in retrieving other fields
    val varId: Long = variable.getId
    val varIndices: List[FieldIndice] = fieldIndiceDao.findById_FieldInstId(varId).asScala.toList
    val varIndex: FieldIndice = varIndices.head
    val varIndexValue: String = varIndex.getIndexValue
    //iterate over each field in the profile and find the instance using the indexed xpath
    for (fieldId <- fieldIds) {
      //get ordering for display
      val ordering: Int = getOrdering("vardetails", fieldId)
      val currentField: Field = fieldDao.findOne(fieldId)
      //get the general (with wildcards) xpath for each field
      val maps: List[Mapping] = mappingDao.findById_FieldId(fieldId).asScala.toList
      if (maps.size != 1) {
        println("failed to find map for field " + fieldId)
      }
      else {
        val map: Mapping = maps.head
        val generalXpath: String = map.getXpath
        //replace wildcard with index
        val indexedXpath: String = generalXpath.replace("*", varIndexValue)
        //find the instance using the indexed xpath
        val insts: List[FieldInst] =
          fieldInstDao.findByRawDocIdAndCanonicalXpath(handle, indexedXpath).asScala.toList
        if (insts.size != 1) {
          println("failed to find field instance with xpath " + indexedXpath)
        }
        else {
          val inst: FieldInst = insts.head
          val key: (String, Int) = (currentField.getDisplayName, ordering)
          //add to hashmap; key is display name of field and value is the text value of the FieldInst
          details.put(key, inst.getValue)
        }
      }

    }
    details.toMap
  }

  /** *** Private utility functions  ******/
  private def getProfileFieldIds(profileId: String): List[String] = {
    val proFields: List[ProfileField] = profileFieldDao.findByProfileId(profileId)
      .asScala.toList
    proFields.map(pf =>pf.getFieldId)
  }

  private def getOrdering(profileId: String, fieldId: String): Int = {
    val pfs: List[ProfileField] =
      profileFieldDao.findByProfileIdAndFieldId(profileId, fieldId).asScala.toList
    if (pfs.size != 1) 99
    else {
      val pf: ProfileField = pfs.head
      pf.getOrdering
    }
  }
}