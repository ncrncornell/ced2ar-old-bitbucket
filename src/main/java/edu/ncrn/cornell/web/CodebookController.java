package edu.ncrn.cornell.web;

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
	private CodebookService codebookService;

    @Autowired
    private CodebooksView codebooksView;
    @Autowired
    private CodebookView codebookView;
    @Autowired
    private VarsView varsView;
    @Autowired
    private VarView varView;

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
	@RequestMapping(value = "/codebooks/{c:.+}",
					method = RequestMethod.GET,
					produces = MediaType.TEXT_HTML_VALUE
				)
	public String codebook(@PathVariable(value = "c") String handle, 
			@RequestParam(value = "auth", defaultValue = "false") boolean auth,
			Model model){
		
		System.out.println("[codebook controller]:: GET request for handle "+handle);
		Map<Tuple2<String, Integer>, String> codebookDetails = codebookService.getCodebookDetails(handle);

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
	@RequestMapping(value = "/codebooks/{c:.+}/vars",
					method = RequestMethod.GET,
					produces = MediaType.TEXT_HTML_VALUE
				   )
	public String vars(@PathVariable(value = "c") String handle,
			@RequestParam(value = "auth", defaultValue = "false") boolean auth,
			Model model){

		Map<String, String> codebookVars = codebookService.getCodebookVariables(handle);
		
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
	@RequestMapping(value = "/codebooks/{c:.+}/vars/{v}",
					method = RequestMethod.GET,
					produces = MediaType.TEXT_HTML_VALUE
					)
	public String var(@PathVariable(value = "c") String handle,
			@PathVariable(value = "v") String varname,
			@RequestParam(value = "auth", defaultValue = "false") boolean auth,
			Model model){
		
		Map<Tuple2<String,Integer>, String> varDetails = codebookService.getVariableDetails(handle, varname);
		
		
		return varView.variableDetails(varDetails, handle, varname);
	}
	
}
