package edu.ncrn.cornell.web;

import edu.ncrn.cornell.Ced2arApplication;
import edu.ncrn.cornell.service.CodebookService;
import edu.ncrn.cornell.view.UploadView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;


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
    @ResponseBody
    @RequestMapping(
        method = RequestMethod.GET,
        value = "/upload",
        produces = MediaType.TEXT_HTML_VALUE
    )
    public String provideUploadInfo() {
        Optional<File[]> folderFilesMaybe = Optional.ofNullable(
            (new File(Ced2arApplication.UPLOAD_DIR)).listFiles()
        );


        return folderFilesMaybe.map(files -> {
            List<String> uploadedFiles = Arrays.stream(files)
                .sorted(Comparator.comparingLong(f -> -1 * f.lastModified()))
                .map(f -> f.getName())
                .collect(Collectors.toList());
            return UploadView.getView(uploadedFiles);
        }).orElse(UploadView.noUploadDir());

    }


    //PUT page
}
