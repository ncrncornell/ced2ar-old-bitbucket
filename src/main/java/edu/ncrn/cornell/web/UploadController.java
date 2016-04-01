package edu.ncrn.cornell.web;

import edu.ncrn.cornell.service.CodebookService;
import edu.ncrn.cornell.view.UploadView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class to handle the upload interface for new codebooks.
 * @author kylebrumsted
 *
 */
@Controller
public class UploadController {

    //TODO: Testing only:
    @Autowired
    CodebookService codebookService;
	
	//GET page
    @RequestMapping(
        value = "/upload",
        method = RequestMethod.GET,
        produces = MediaType.TEXT_HTML_VALUE
    )
    @ResponseBody
    public String uploadGet(Model model) {
        return UploadView.getView(model);
    }

    //PUT page
}
