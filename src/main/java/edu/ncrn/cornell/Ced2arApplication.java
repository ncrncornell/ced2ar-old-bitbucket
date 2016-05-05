package edu.ncrn.cornell;


import edu.ncrn.cornell.config.Ced2arConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.view.InternalResourceViewResolver;


@SpringBootApplication
public class Ced2arApplication {

	private static ApplicationContext ctx;

    public static void main(String[] args) {
    	ctx = SpringApplication.run(Ced2arApplication.class, args);
    }
    
    //TODO: remove or simplify when JSP removed?
    @Bean
	public InternalResourceViewResolver setupViewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".jsp");
		return resolver;
	}

}
