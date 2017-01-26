package edu.ncrn.cornell.site.controller

import java.util.{Map => JavaMap}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import edu.ncrn.cornell.service.CodebookService
import edu.ncrn.cornell.site.view.CodebookView
import edu.ncrn.cornell.site.view.CodebooksView
import edu.ncrn.cornell.site.view.VarView
import edu.ncrn.cornell.site.view.VarsView
import edu.ncrn.cornell.site.view.AllVarsView

@Controller class CodebookController {
  @Autowired private val codebookService: CodebookService = null
  @Autowired private val codebooksView: CodebooksView = null
  @Autowired private val codebookView: CodebookView = null
  @Autowired private val varsView: VarsView = null
  @Autowired private val varView: VarView = null
  @Autowired private val allVarsView: AllVarsView = null

  /**
    * controller for all codebooks page
    *
    * @param model
    * @return
    */
  @ResponseBody
  @RequestMapping(
    method = Array(RequestMethod.GET),
    value = Array("/codebooks"),
    produces = Array(MediaType.TEXT_HTML_VALUE)
  )
  def codebooks(
    model: Model,
    @RequestParam(value = "auth", defaultValue = "false") auth: Boolean
  ): String = {
    val handles: JavaMap[String, String] = codebookService.getAllHandles
    codebooksView.codebooksList(handles)
  }

  /**
    * Controller for codebook titlepage
    *
    * @param handle
    * @param auth
    * @param model
    * @return
    */
  @ResponseBody
  @RequestMapping(
    value = Array("/codebooks/{c:.+}"),
    method = Array(RequestMethod.GET),
    produces = Array(MediaType.TEXT_HTML_VALUE)
  )
  def codebook(
    @PathVariable(value = "c") handle: String,
    @RequestParam(value = "auth", defaultValue = "false") auth: Boolean,
    model: Model
  ): String = {
    println("[codebook controller]:: GET request for handle " + handle)
    val codebookDetails: JavaMap[(String, Integer), String] =
      codebookService.getCodebookDetails(handle)
    codebookView.codebookDetails(codebookDetails, handle)
  }

  /**
    * Controller for list of variables in a codebook
    *
    * @param handle
    * @param auth
    * @param model
    * @return
    */
  @ResponseBody
  @RequestMapping(
    value = Array("/codebooks/{c:.+}/vars"),
    method = Array(RequestMethod.GET),
    produces = Array(MediaType.TEXT_HTML_VALUE)
  )
  def vars(
    @PathVariable(value = "c") handle: String,
    @RequestParam(value = "auth", defaultValue = "false") auth: Boolean,
    model: Model
  ): String = {
    val codebookVars: JavaMap[String, String] =
      codebookService.getCodebookVariables(handle)
    varsView.varsList(codebookVars, handle)
  }

  /**
    * Controller for variable details page
    *
    * @param handle
    * @param varname
    * @param auth
    * @param model
    * @return
    */
  @ResponseBody
  @RequestMapping(
    value = Array("/codebooks/{c:.+}/vars/{v}"),
    method = Array(RequestMethod.GET),
    produces = Array(MediaType.TEXT_HTML_VALUE)
  )
  def variable(
    @PathVariable(value = "c") handle: String,
    @PathVariable(value = "v") varname: String,
    @RequestParam(value = "auth", defaultValue = "false") auth: Boolean,
    model: Model
  ): String = {
    val varDetails: JavaMap[(String, Integer), String] =
      codebookService.getVariableDetails(handle, varname)
    varView.variableDetails(varDetails, handle, varname)
  }

  @ResponseBody
  @RequestMapping(
    value = Array("/vars"),
    method = Array(RequestMethod.GET),
    produces = Array(MediaType.TEXT_HTML_VALUE)
  )
  def allVars(
    @RequestParam(value = "auth", defaultValue = "false") auth: Boolean,
    model: Model
  ): String = {
    val variables: JavaMap[String, (String, String)] = codebookService.getAllVariables
    allVarsView.allVarsList(variables)
  }
}