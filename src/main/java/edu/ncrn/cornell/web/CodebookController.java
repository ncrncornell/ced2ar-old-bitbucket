package edu.ncrn.cornell.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import scala.Tuple2;

import edu.ncrn.cornell.service.CodebookService;
import edu.ncrn.cornell.view.CodebookView;
import edu.ncrn.cornell.view.CodebooksView;
import edu.ncrn.cornell.view.VarView;
import edu.ncrn.cornell.view.VarsView;

@Controller
public class CodebookController {

	@Autowired
	CodebookService codebookService;
	
	CodebooksView codebooksView = new CodebooksView();
	CodebookView codebookView = new CodebookView();
	VarsView varsView = new VarsView();
	VarView varView = new VarView();
	
	/**
	 * controller for all codebooks page
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(
	        method = RequestMethod.GET,
	        value = "/codebooks",
	        produces = MediaType.TEXT_HTML_VALUE
	    )
	public String codebooks(Model model,
			@RequestParam(value = "auth", defaultValue = "false") boolean auth){
		
		Map<String, String> handles = codebookService.getAllHandles();
		
		model.addAttribute("handles", handles);
		model.addAttribute("auth", auth);
		
		return codebooksView.codebooksList(handles);
	}
	
	
	
	/**
	 * Controller for codebook titlepage
	 * @param handle
	 * @param auth
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/codebooks/{c}",
					method = RequestMethod.GET,
					produces = MediaType.TEXT_HTML_VALUE
				)
	public String codebook(@PathVariable(value = "c") String handle, 
			@RequestParam(value = "auth", defaultValue = "false") boolean auth,
			Model model){
		System.out.println("controller for codebook details called with handle ["+handle+"]");
		Map<Tuple2<String, Integer>, String> codebookDetails = codebookService.getCodebookDetails(handle);
		
		model.addAttribute("handle", handle);
		model.addAttribute("details", codebookDetails);
		
		//return "codebook";
		return codebookView.codebookDetails(codebookDetails, handle);
	}
	
	/**
	 * Controller for list of variables in a codebook
	 * @param handle
	 * @param auth
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/codebooks/{c}/vars",
					method = RequestMethod.GET,
					produces = MediaType.TEXT_HTML_VALUE
				   )
	public String vars(@PathVariable(value = "c") String handle,
			@RequestParam(value = "auth", defaultValue = "false") boolean auth,
			Model model){
		
		Map<String, String> codebookVars = codebookService.getCodebookVariables(handle);
		
		System.out.println("[Var List]:: controller called for "+handle);
		
		model.addAttribute("handle", handle);
		model.addAttribute("variables", codebookVars);
		
		return varsView.varsList(codebookVars, handle);
	}
	
	/**
	 * Controller for variable details page
	 * @param handle
	 * @param varname
	 * @param auth
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/codebooks/{c}/vars/{v}",
					method = RequestMethod.GET,
					produces = MediaType.TEXT_HTML_VALUE
					)
	public String var(@PathVariable(value = "c") String handle,
			@PathVariable(value = "v") String varname,
			@RequestParam(value = "auth", defaultValue = "false") boolean auth,
			Model model){
		
		Map<Tuple2<String,Integer>, String> varDetails = codebookService.getVariableDetails(handle, varname);
		
		model.addAttribute("handle", handle);
		model.addAttribute("varname", varname);
		model.addAttribute("details", varDetails);
		
		return varView.variableDetails(varDetails, handle, varname);
	}
	
}
