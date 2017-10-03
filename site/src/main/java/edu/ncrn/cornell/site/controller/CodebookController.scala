package edu.ncrn.cornell.site.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation._
import edu.ncrn.cornell.service.CodebookService
import edu.ncrn.cornell.site.view._

@Autowired
@Controller
@CrossOrigin //TODO: get allowed origins from config properties
class CodebookController(
  private[controller] val appView: AppView,
  private[controller] val codebookService: CodebookService,
  private[controller] val codebooksView: CodebooksView,
  private[controller] val codebookView: CodebookView,
  private[controller] val varsView: VarsView,
  private[controller] val varView: VarView
) {

  /**
    * controller for all codebooks page
    *
    * @param model
    * @return
    */
  @ResponseBody
  @RequestMapping(
    method = Array(RequestMethod.GET),
    value = Array("/codebook"),
    produces = Array(MediaType.TEXT_HTML_VALUE)
  )
  def codebooks(
    model: Model,
    @RequestParam(value = "auth", defaultValue = "false") auth: Boolean
  ): String = {
    val handles: Map[String, String] = codebookService.getAllHandles
    codebooksView.codebooksList(handles)
  }
  //
  @ResponseBody
  @RequestMapping(
    value = Array("/codebook"),
    method = Array(RequestMethod.GET),
    produces = Array(MediaType.APPLICATION_JSON_UTF8_VALUE)
  )
  def codebooksJson(
    model: Model,
    @RequestParam(value = "auth", defaultValue = "false") auth: Boolean
  ): String = codebookService.getAllHandlesJson


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
    value = Array("/codebook/{c:.+}"),
    method = Array(RequestMethod.GET),
    produces = Array(MediaType.TEXT_HTML_VALUE)
  )
  def codebook(
    @PathVariable(value = "c") handle: String,
    @RequestParam(value = "auth", defaultValue = "false") auth: Boolean,
    model: Model
  ): String = {
    println("[codebook controller]:: GET request for handle " + handle)
    val codebookDetails:  List[(String,List[String])]=
      codebookService.getCodebookDetailsList(handle)
    codebookView.codebookDetails(codebookDetails, handle)
  }
  //
  @ResponseBody
  @RequestMapping(
    value = Array("/codebook/{c:.+}"),
    method = Array(RequestMethod.GET),
    produces = Array(MediaType.APPLICATION_JSON_UTF8_VALUE)
  )
  def codebookJson(
    @PathVariable(value = "c") handle: String,
    @RequestParam(value = "auth", defaultValue = "false") auth: Boolean,
    model: Model
  ): String = codebookService.getCodebookDetailsListJson(handle)


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
    value = Array("/codebook/{c:.+}/var"),
    method = Array(RequestMethod.GET),
    produces = Array(MediaType.TEXT_HTML_VALUE)
  )
  def vars(
    @PathVariable(value = "c") handle: String,
    @RequestParam(value = "auth", defaultValue = "false") auth: Boolean,
    model: Model
  ): String = {
    val codebookVars: Map[String, (String, String)] =
      codebookService.getCodebookVariables(handle)
    varsView.varsList(codebookVars)
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
    value = Array("/codebook/{c:.+}/var/{v}"),
    method = Array(RequestMethod.GET),
    produces = Array(MediaType.TEXT_HTML_VALUE)
  )
  def variable(
    @PathVariable(value = "c") handle: String,
    @PathVariable(value = "v") varname: String,
    @RequestParam(value = "auth", defaultValue = "false") auth: Boolean,
    model: Model
  ): String = {
    val varDetails: List[(String, List[String])] =
      codebookService.getVariableDetailsList(handle, varname)
    varView.variableDetails(varDetails, handle, varname)
  }
  //
  @ResponseBody
  @RequestMapping(
    value = Array("/codebook/{c:.+}/var/{v}"),
    method = Array(RequestMethod.GET),
    produces = Array(MediaType.APPLICATION_JSON_UTF8_VALUE)
  )
  def variableJson(
    @PathVariable(value = "c") handle: String,
    @PathVariable(value = "v") varname: String,
    @RequestParam(value = "auth", defaultValue = "false") auth: Boolean,
    model: Model
  ): String = codebookService.getVariableDetailsListJson(handle, varname)

  @ResponseBody
  @RequestMapping(
    value = Array("/var"),
    method = Array(RequestMethod.GET),
    produces = Array(MediaType.TEXT_HTML_VALUE)
  )
  def allVars(
    @RequestParam(value = "auth", defaultValue = "false") auth: Boolean,
    model: Model
  ): String = {
    val variables: Map[String, (String, String)] = codebookService.getAllVariables
    varsView.varsList(variables)
  }
}