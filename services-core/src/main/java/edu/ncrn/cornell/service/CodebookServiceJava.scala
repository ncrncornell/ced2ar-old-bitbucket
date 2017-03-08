package edu.ncrn.cornell.service

import java.util

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import collection.JavaConverters._

import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._

/**
  * Created by Brandon on 1/27/2017.
  */

@Service
@Autowired
class CodebookServiceJava(private[service] val codebookService: CodebookService) {

  /**
    * Lists all handles and full names
    *
    * @return
    */
  def getAllHandles: util.Map[String, String] = codebookService.getAllHandles.asJava

  def getAllHandlesJson: String = codebookService.getAllHandlesJson


  /**
    * Retrieves all variables in the database
    *
    * @return a map of (name,label) pairs
    */
  def getAllVariables: util.Map[String, (String, String)] =
    codebookService.getAllVariables.asJava

  def getAllVariablesJson: String = codebookService.getAllVariablesJson


  /**
    * Gets the list of variables for a given codebook (name, label) pairs
    * The profile of this list is comprised of varname and varlabel.
    * This profile is currently hardcoded into the function.
    * TODO: generate profile dynamically
    *
    * @param handle
    * @return
    */
  def getCodebookVariables(handle: String): util.Map[String, (String, String)] =
    codebookService.getCodebookVariables(handle).asJava

  def getCodebookVariablesJson(handle: String): String =
    codebookService.getCodebookVariablesJson(handle)

  def getCodebookVariables(handle: String, page: Integer)
  : util.Map[String, (String, String)] =
    codebookService.getCodebookVariables(handle, page).asJava

  def getCodebookVariablesJson(handle: String, page: Integer): String =
    codebookService.getCodebookVariablesJson(handle, page)

  /**
    * retrieves variable details profile from SQL tables
    *
    * @param handle
    * @param varname
    * @return
    */
  def getVariableDetailsList(handle: String, varname: String)
  :  util.List[(String, util.List[String])] =
    codebookService.getVariableDetailsList(handle, varname).map{
      case (key, value) => (key, value.asJava)
    }.asJava

}
