package edu.ncrn.cornell.site.view

import edu.ncrn.cornell.site.view.common.Ced2arView
import org.springframework.stereotype.Component

//import scalatags.Text.TypedTag
//import scalatags.Text.all._


@Component
class AppView extends Ced2arView {

//  lazy val appContainer: String = {
//    val typedHtml = html(
//      head(
//        defaultMetaTags,
//        defaultStyleSheetsAndScripts
//      ),
//      body(
//        masterDiv(
//          div(id := "application-container"),
//          script(
//            `type` := "application/javascript",
//            src := //FIXME: config to use fullopt from pom.
//            s"$servletPath/scalajs-bundler/main/ced2ar3-view-fastopt-bundle.js"
//          )
//        )
//      )
//    )
//    typedHtml.toString()
//  }
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
    |  <div id="application-container"></dib>
    |  <script type="application/javascript"
    |    src="$servletPath/scalajs-bundler/main/ced2ar3-view-fastopt-bundle.js"
    |  ></script>
    |</body>
    |</html>
  """.stripMargin
  
}