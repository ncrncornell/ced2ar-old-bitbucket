package edu.ncrn.cornell.site

import org.hibernate.validator.constraints.NotBlank
import org.springframework.boot.context.properties.{ConfigurationProperties, EnableConfigurationProperties}
import org.springframework.context.annotation.Configuration

import scala.beans.BeanProperty

/**
  * @author Brandon Barker
  *
  *         Created on 5/5/2016.
  */
@Configuration
//@EnableConfigurationProperties
@ConfigurationProperties(prefix = "ced2ar")
class Ced2arConfig {

  //TODO: how to make this boolean?
  @NotBlank
  @BeanProperty
  var checkDb: String = _

  @NotBlank
  @BeanProperty
  var uploadDir: String = _

  @NotBlank
  @BeanProperty
  var version: String = _


}