package edu.ncrn.cornell.site.view

import edu.ncrn.cornell.site.view.common.Ced2arView
import org.springframework.stereotype.Component
import AppView._

@Component
class AppView extends Ced2arView {

lazy val appContainer: String =
  s"""
    |<!DOCTYPE html>
    |<html>
    |<head>
    |    <meta charset="UTF-8">
    |    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    |    <title>CED2AR</title>
    |</head>
    |<body>
    |  <div id="application-container"></div>
    |  <div id="body-scripts"></div>
    |  <script type="application/javascript"
    |    src="$servletPath/target/scala-$scalaMajorVer/scalajs-bundler/main/ced2ar3-view-fastopt-bundle.js"
    |  ></script>
    |</body>
    |</html>
  """.stripMargin
  
}

object AppView {

  protected val rmMinorVer = "\\.[0-9]*$"
  protected val scalaMajorVer = scala.util.Properties.scalaPropOrElse("version.number", "unknown")
    .replaceFirst(rmMinorVer, "")

}