name := "ced2ar3-view"

scalaVersion in ThisBuild := "2.11.8" // or any other Scala version >= 2.10.2

//lazy val mhtmlV = "latest.integration"
lazy val mhtmlV = "0.1.1"


lazy val view = (project in file(".")).enablePlugins(ScalaJSPlugin)
  .settings(
    libraryDependencies ++= Seq(
      "in.nvilla" %%% "monadic-rx-cats" % mhtmlV,
      "in.nvilla" %%% "monadic-html" % mhtmlV,
      "com.lihaoyi" %%% "upickle" % "0.3.9"
    )
  )
