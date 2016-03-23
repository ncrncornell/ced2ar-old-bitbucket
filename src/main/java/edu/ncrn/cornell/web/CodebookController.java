package edu.ncrn.cornell.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import edu.ncrn.cornell.service.CodebookService;

@Controller
public class CodebookController {

	@Autowired
	CodebookService codebookService;
	
	/**
	 * controller for all codebooks page
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/codebooks", method = RequestMethod.GET)
	public String codebooks(Model model,
			@RequestParam(value = "auth", defaultValue = "false") boolean auth){
		
		List<String> handles = codebookService.getAllHandles();
		
		model.addAttribute("handles",handles);
		model.addAttribute("auth",auth);
		
		return "codebooks";
	}
	
	
	
	/**
	 * Controller for codebook titlepage
	 * @param handle
	 * @param auth
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/codebooks/{c}", method = RequestMethod.GET)
	public String codebook(@PathVariable(value = "c") String handle, 
			@RequestParam(value = "auth", defaultValue = "false") boolean auth,
			Model model){
		
		Map<String, String> codebookDetails = codebookService.getCodebookDetails(handle);
		
		model.addAttribute("handle", handle);
		model.addAttribute("details", codebookDetails);
		
		return "codebook";
	}
	
	/**
	 * Controller for list of variables in a codebook
	 * @param handle
	 * @param auth
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/codebooks/{c}/vars", method = RequestMethod.GET)
	public String vars(@PathVariable(value = "c") String handle,
			@RequestParam(value = "auth", defaultValue = "false") boolean auth,
			Model model){
		
		Map<String, String> codebookVars = codebookService.getCodebookVariables(handle);
		
		model.addAttribute("handle", handle);
		model.addAttribute("variables", codebookVars);
		
		return "vars";
	}
	
	/**
	 * Controller for variable details page
	 * @param handle
	 * @param varname
	 * @param auth
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/codebooks/{c}/vars/{v}", method = RequestMethod.GET)
	public String var(@PathVariable(value = "c") String handle,
			@PathVariable(value = "v") String varname,
			@RequestParam(value = "auth", defaultValue = "false") boolean auth,
			Model model){
		
		Map<String, String> varDetails = codebookService.getVariableDetails(handle, varname);
		
		model.addAttribute("handle", handle);
		model.addAttribute("varname", varname);
		model.addAttribute("details", varDetails);
		
		return "var";
	}
	
}
