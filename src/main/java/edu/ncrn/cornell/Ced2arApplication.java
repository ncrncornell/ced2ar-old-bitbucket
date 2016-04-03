package edu.ncrn.cornell;


import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.view.InternalResourceViewResolver;


@SpringBootApplication
public class Ced2arApplication{

	public static String UPLOAD_DIR = "upload-dir";
	
    public static void main(String[] args) {
    	SpringApplication.run(Ced2arApplication.class, args);
    }
    
    
    @Bean
	public InternalResourceViewResolver setupViewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".jsp");
		return resolver;
	}

}
