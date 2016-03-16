package edu.ncrn.cornell.web;

import java.util.ArrayList;
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
	public String codebooks(Model model){
		
		List<String> handles = codebookService.getAllHandles();
		model.addAttribute("handles",handles);
		boolean auth = false;
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
		
		/*
		for(String k : codebookDetails.keySet()){
			System.out.println("key: "+k+", value: "+codebookDetails.get(k));
		}
		*/
		
		model.addAttribute("details", codebookDetails);
		
		return "codebook";
	}
	
}
