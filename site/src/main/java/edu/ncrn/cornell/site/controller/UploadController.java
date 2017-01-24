package edu.ncrn.cornell.site.controller;

import edu.ncrn.cornell.site.Ced2arConfig;
import edu.ncrn.cornell.service.UploadService;
import edu.ncrn.cornell.site.view.UploadView;
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
    private UploadService uploadService;

    @Autowired
    private Ced2arConfig ced2arConfig;

    @Autowired
    UploadView view;

    private final String thisPath = "/upload";
	
	//GET page
    @ResponseBody
    @RequestMapping(
        method = RequestMethod.GET,
        value = thisPath,
        produces = MediaType.TEXT_HTML_VALUE
    )
    public String provideUploadInfo() {
        File uploadDir = new File(ced2arConfig.getUploadDir());
        Optional<File[]> folderFilesMaybe = Optional.ofNullable((uploadDir).listFiles());

        //TODO: change to log message:
        System.out.println(uploadDir.getAbsolutePath());

        //TODO: file list for developer-mode only (add or use a developer/debug-mode flag)
        return folderFilesMaybe.map(files -> {
            List<String> uploadedFiles = Arrays.stream(files)
                .sorted(Comparator.comparingLong(f -> -1 * f.lastModified()))
                .map(f -> f.getName())
                .collect(Collectors.toList());
            return view.uploadForm(uploadedFiles);
        }).orElse(view.noUploadDir(uploadDir.getAbsolutePath()));
    }


    //POST page
    @RequestMapping(
        method = RequestMethod.POST,
        value = thisPath
    )
    public String handleFileUpload(@RequestParam("name") String name,
                                   @RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {
        if (name.contains("/")) {
            redirectAttributes.addFlashAttribute("message", "Folder separators not allowed");
            return "redirect:" + thisPath;
        }
        if (name.contains("/")) {
            redirectAttributes.addFlashAttribute("message", "Relative pathnames not allowed");
            return "redirect:" + thisPath;
        }

        if (!file.isEmpty()) {
            try {
                File newXmlFile = new File(ced2arConfig.getUploadDir() + "/" + name);
                BufferedOutputStream stream = new BufferedOutputStream(
                        new FileOutputStream(newXmlFile));
                FileCopyUtils.copy(file.getInputStream(), stream);
                stream.close();
                //Process file
                uploadService.newUpload(newXmlFile);

                if (uploadService.importSucceded()) {
                    redirectAttributes.addFlashAttribute("success",
                            "You successfully uploaded " + name + "!");
                }
                else {
                    if (!uploadService.uploadIsValid()) {
                        redirectAttributes.addFlashAttribute("error",
                                "There was a problem parsing XML for " + name + "!");
                    }
                    else {
                        redirectAttributes.addFlashAttribute("error",
                                "There was an unknown problem with importing " + name + "!");
                    }
                }
            }
            catch (Exception e) {
                redirectAttributes.addFlashAttribute("error",
                        "You failed to upload " + name + " => " + e.getMessage());
            }
        }
        else {
            redirectAttributes.addFlashAttribute("error",
                    "You failed to upload " + name + " because the file was empty");
        }

        view.setAttributes(redirectAttributes);

        return "redirect:" + thisPath;

    }
}
