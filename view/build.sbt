name := "ced2ar3-view"

scalaVersion in ThisBuild := "2.11.8" // or any other Scala version >= 2.10.2
lazy val mhtmlV = "0.1.1"



//artifactPath in (Compile, packageScalaJSLauncher) :=
//  (resourceManaged in Compile).value / "public"
////configure a specific directory for scalajs output

//val scalajsOutputDir = Def.settingKey[File]("directory for javascript files output by scalajs")


lazy val view = project.enablePlugins(ScalaJSPlugin, ScalaJSWeb)
  .settings(
    libraryDependencies ++= Seq(
      "in.nvilla" %%% "monadic-rx-cats" % mhtmlV,
      "in.nvilla" %%% "monadic-html" % mhtmlV,
      "com.lihaoyi" %%% "upickle" % "0.3.9"
    )
  )

lazy val server = project
  .settings(
    scalaJSProjects := Seq(view),
    pipelineStages in Assets := Seq(scalaJSPipeline),
    WebKeys.packagePrefix in Assets := "public/",
    WebKeys.exportedMappings in Assets ++= (for ((file, path) <- (mappings in Assets).value)
      yield file -> ((WebKeys.packagePrefix in Assets).value + path))
  )
  .enablePlugins(SbtWeb)
