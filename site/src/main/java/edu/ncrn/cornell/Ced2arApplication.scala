package edu.ncrn.cornell

import org.springframework.boot._
import org.springframework.boot.autoconfigure._
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.{Bean, ComponentScan}
import org.springframework.web.servlet.view.InternalResourceViewResolver

@SpringBootApplication
class Ced2arApplication {

  //TODO: remove or simplify when JSP removed?
  @Bean
  def setupViewResolver: InternalResourceViewResolver = {
    val resolver: InternalResourceViewResolver = new InternalResourceViewResolver
    resolver.setPrefix("/WEB-INF/views/")
    resolver.setSuffix(".jsp")
    resolver
  }
}

object Ced2arApplication {
  private var ctx: ApplicationContext = _

  def main(args: Array[String]) {
    ctx = SpringApplication.run(classOf[Ced2arApplication], args: _*)
  }
}