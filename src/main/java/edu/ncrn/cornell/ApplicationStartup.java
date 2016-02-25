package edu.ncrn.cornell;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import edu.ncrn.cornell.util.DBChecker;

/**
 * Class to execute custom code upon app startup
 * Used now to check database status.
 * 
 * @author kylebrumsted
 *
 */
@Component
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {
	
	//bean injection for database health
	@Autowired
	DBChecker dbChecker;
	
	
	@Override
	public void onApplicationEvent(final ContextRefreshedEvent event)
	{
		
		System.out.println("[ApplicationStartup]:: This is a test for executing code on application startup");
		dbChecker.DBinit();
		return;
	}
}
