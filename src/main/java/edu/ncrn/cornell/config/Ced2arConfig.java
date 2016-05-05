package edu.ncrn.cornell.config;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Brandon Barker
 *
 * Created on 5/5/2016.
 */
@Configuration
@ConfigurationProperties(prefix = "ced2ar")
public class Ced2arConfig {
    @NotBlank
    private String uploadDir;
    //
    public void setUploadDir(String dir) {
        uploadDir = dir;
    }
    //
    public String getUploadDir() {
        return uploadDir;
    }




}
