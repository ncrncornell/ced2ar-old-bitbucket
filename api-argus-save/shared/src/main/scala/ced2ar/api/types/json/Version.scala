package ced2ar.api.types.json

/**
  * @author brand
  *         3/17/2017
  */

//TODO: rewrite using Argus

//TODO: consider - do we want to use Maven versioning? see https://docs.oracle.com/middleware/1212/core/MAVEN/maven_version.htm#MAVEN400
case class Version(major: Int, minor: Int, patch: Int)