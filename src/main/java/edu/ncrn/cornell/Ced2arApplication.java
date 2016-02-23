package edu.ncrn.cornell;


import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.InternalResourceViewResolver;


@EnableAutoConfiguration
@Configuration
@ComponentScan
//@SpringBootApplication
public class Ced2arApplication /* extends SpringBootServletInitializer*/{
	
	/*
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Ced2arApplication.class);
    }
	*/
	
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
