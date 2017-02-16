package edu.ncrn.cornell.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
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

import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._

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
@Autowired
class CodebookService(
  private[service] val rawDocDao: RawDocDao,
  private[service] val profileDao: ProfileDao,
  private[service] val fieldDao: FieldDao,
  private[service] val profileFieldDao: ProfileFieldDao,
  private[service] val mappingDao: MappingDao,
  private[service] val schemaDao: SchemaDao,
  private[service] val fieldInstDao: FieldInstDao,
  private[service] val fieldIndiceDao: FieldIndiceDao
) {

  //externalize to properties once finished testing
  private val PAGE_SIZE = 25
  
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

  def getAllHandlesJson: String = getAllHandles.asJson.noSpaces

  
  /**
   * Returns an ordered list of pairs of the field display names with their content.
   * 
   */
  def getCodebookDetailsList(handle: String): List[(String,List[String])] = {
    val fieldIds: List[String] = getProfileFieldIds("codebookdetails")
    val details = new mutable.ArrayBuffer[(String, List[String])]
    for(i <-fieldIds.indices) {
      details += null
    }
    
    for (fieldId <- fieldIds) {
      
      val ordering: Int = getOrdering("codebookdetails", fieldId)
      val curField: Field = fieldDao.findOne(fieldId)
      val dispName: String = curField.getDisplayName.trim
      val fieldInsts: List[FieldInst] = fieldInstDao.findByRawDocIdAndFieldId(handle, fieldId)
        .asScala.toList
        
      if (fieldInsts.nonEmpty) {
        var values: List[String] = List()
        for (fi <- fieldInsts) values :+ fi.getValue
        details(ordering-1) = (dispName, values)
      }
      else {
        println("[READING FIELDISNTS]:: No FieldInst for codebook " + handle + " field " + fieldId)
      }
    }
    
    compress(details.toList)
  }
  


 

  /**
    * gathers codebook details from FieldInst table rather than parsing XML
    *
    * @param handle
    * @return
    */
  @deprecated
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
        for (fi <- fieldInsts) value += fi.getValue + " \n"
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
    getVarList(handles, 0)
  }

  def getAllVariablesJson: String = getAllVariables.asJson.noSpaces

  /**
    * Gets the list of variables for a given codebook (name, label) pairs
    * The profile of this list is comprised of varname and varlabel.
    * This profile is currently hardcoded into the function.
    * TODO: generate profile dynamically
    *
    * @param handle
    * @return
    */
  def getCodebookVariables(handle: String): Map[String, (String, String)] =
    getVarList(List(handle), 0)

  def getCodebookVariablesJson(handle: String): String =
    getCodebookVariables(handle).asJson.noSpaces

    
  def getCodebookVariables(handle: String, page: Int): Map[String, (String, String)] =
    getVarList(List(handle), page)

  def getCodebookVariablesJson(handle: String, page: Int): String =
    getCodebookVariables(handle, page).asJson.noSpaces

  /**
   * private function to get paginated list of variables
   */
  private def getVarList(handles: List[String], pageNumber: Integer): Map[String, (String, String)] = {
    val variables: mutable.Map[String, (String, String)] = mutable.Map()
    val request: Pageable = new PageRequest(pageNumber, PAGE_SIZE, Sort.Direction.ASC, "value")
    val varnamesPage: Page[FieldInst] = fieldInstDao.findByFieldIdAndRawDocIdIn("varname", handles.asJava, request)
    val varnames: List[FieldInst] = varnamesPage.getContent.asScala.toList
    //for each varname find the labl and add to hashmap
    for (varname <- varnames) {
      val handle: String = varname.getRawDocId
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
    variables.toMap
  }

  
  /**
   * 
   * retreives variable details from SQL
   * 
   * returns in form of list of tuples (display name, List(values))
   */
  def getVarbleDetailsList(handle: String, varname: String): List[(String,List[String])] = {
    val fieldIds: List[String] = getProfileFieldIds("variabledetails")
    val details = new mutable.ArrayBuffer[(String, List[String])]
    for(i <- 0 to fieldIds.size-1) {
      details += null
    }
    
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
          details(ordering-1) = (currentField.getDisplayName, List(inst.getValue))
        }
      }
    }
    compress(details.toList)
  }
  
  /**
    * retrieves variable details profile from SQL tables
    *
    * @param handle
    * @param varname
    * @return
    */
  @deprecated
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
  
  private def compress[A](l : List[A]) : List[A] = {
    l match{
      case Nil => Nil
      case null::tail => compress(tail)
      case head::tail => head::compress(tail)
    }
  }
}