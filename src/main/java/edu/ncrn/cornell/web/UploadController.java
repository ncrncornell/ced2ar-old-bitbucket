package edu.ncrn.cornell.web;

import edu.ncrn.cornell.Ced2arApplication;
import edu.ncrn.cornell.service.CodebookService;
import edu.ncrn.cornell.service.UploadService;
import edu.ncrn.cornell.view.UploadView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Controller class to handle the upload interface for new codebooks.
 * @author kylebrumsted
 *
 */
@Controller
public class UploadController {

    @Autowired
    UploadService uploadService;
	
	//GET page
    @ResponseBody
    @RequestMapping(
        method = RequestMethod.GET,
        value = "/upload",
        produces = MediaType.TEXT_HTML_VALUE
    )
    public String provideUploadInfo() {
        File uploadDir = new File(Ced2arApplication.UPLOAD_DIR);
        Optional<File[]> folderFilesMaybe = Optional.ofNullable((uploadDir).listFiles());

        //TODO: change to log message:
        System.out.println(uploadDir.getAbsolutePath());

        //TODO: file list for developer-mode only (add or use a developer/debug-mode flag)
        return folderFilesMaybe.map(files -> {
            List<String> uploadedFiles = Arrays.stream(files)
                .sorted(Comparator.comparingLong(f -> -1 * f.lastModified()))
                .map(f -> f.getName())
                .collect(Collectors.toList());
            return UploadView.uploadForm(uploadedFiles);
        }).orElse(UploadView.noUploadDir());
    }


    //POST page
    //@ResponseBody
    @RequestMapping(
        method = RequestMethod.POST,
        value = "/upload"//,
        //produces = MediaType.TEXT_HTML_VALUE
    )
    public String handleFileUpload(@RequestParam("name") String name,
                                   @RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {
        if (name.contains("/")) {
            redirectAttributes.addFlashAttribute("message", "Folder separators not allowed");
            return "redirect:upload";
        }
        if (name.contains("/")) {
            redirectAttributes.addFlashAttribute("message", "Relative pathnames not allowed");
            return "redirect:upload";
        }

        if (!file.isEmpty()) {
            try {
                File newXmlFile = new File(Ced2arApplication.UPLOAD_DIR + "/" + name);
                BufferedOutputStream stream = new BufferedOutputStream(
                        new FileOutputStream(newXmlFile));
                FileCopyUtils.copy(file.getInputStream(), stream);
                stream.close();
                //Process file
                uploadService.newUpload(newXmlFile);

                redirectAttributes.addFlashAttribute("message",
                        "You successfully uploaded " + name + "!");
            }
            catch (Exception e) {
                redirectAttributes.addFlashAttribute("message",
                        "You failed to upload " + name + " => " + e.getMessage());
            }
        }
        else {
            redirectAttributes.addFlashAttribute("message",
                    "You failed to upload " + name + " because the file was empty");
        }

        return "redirect:upload";
    }
}
