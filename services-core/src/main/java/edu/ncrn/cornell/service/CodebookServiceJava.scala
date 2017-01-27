package edu.ncrn.cornell.service

import java.util

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import collection.JavaConverters._

/**
  * Created by Brandon on 1/27/2017.
  */
@Service class CodebookServiceJava {
  @Autowired private val codebookService: CodebookService = null


  /**
    * Lists all handles and full names
    *
    * @return
    */
  def getAllHandles: util.Map[String, String] = codebookService.getAllHandles.asJava


  /**
    * gathers codebook details from FieldInst table rather than parsing XML
    *
    * @param handle
    * @return
    */
  def getCodebookDetails(handle: String): util.Map[(String, Integer), String] =
    codebookService.getCodebookDetails(handle).map(keyValue =>
      ((keyValue._1._1, new Integer(keyValue._1._2)), keyValue._2)
    ).asJava


  /**
    * Retrieves all variables in the database
    *
    * @return a map of (name,label) pairs
    */
  def getAllVariables: util.Map[String, (String, String)] =
    codebookService.getAllVariables.asJava

  /**
    * Gets the list of variables for a given codebook (name, label) pairs
    * The profile of this list is comprised of varname and varlabel.
    * This profile is currently hardcoded into the function.
    * TODO: generate profile dynamically
    *
    * @param handle
    * @return
    */
  def getCodebookVariables(handle: String): util.Map[String, String] =
    codebookService.getCodebookVariables(handle).asJava

  /**
    * retrieves variable details profile from SQL tables
    *
    * @param handle
    * @param varname
    * @return
    */
  def getVariableDetails(handle: String, varname: String)
  : util.Map[(String, Integer), String] =
    codebookService.getVariableDetails(handle, varname)
      .map(keyValue => ((keyValue._1._1, new Integer(keyValue._1._2)), keyValue._2)).asJava



}
