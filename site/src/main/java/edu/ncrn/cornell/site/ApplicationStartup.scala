package edu.ncrn.cornell.site

import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Component
import edu.ncrn.cornell.model.testing.DBChecker
import org.springframework.beans.factory.annotation.Autowired

/**
  * Class to execute custom code upon app startup
  * Used now to check database status.
  *
  * @author kylebrumsted
  *
  */
@Component
class ApplicationStartup
  extends ApplicationListener[ContextRefreshedEvent] {

  @Autowired
  var ced2arConfig: Ced2arConfig = _

  @Autowired
  var dBChecker: DBChecker = _

  def onApplicationEvent(event: ContextRefreshedEvent) {

    if (ced2arConfig.checkDb.toBoolean) {
      dBChecker.DBinitIsOk()
    }
    // Note that we used to run DBChecker.DBinitIsOk() here
    // in order to initialize/check the database. However, this class has migrated to testing
    // code since production databases can't be initialized like this (their data may be
    // site-dependent). If you are creating a database to test, uncomment the following lines
  }
}