name := "ced2ar3-view"

//TODO: set this from maven; also note setting sjs plugin version in plugins.sbt
scalaVersion in ThisBuild := "2.12.1" // or any other Scala version >= 2.10.2

lazy val mhtmlV = "latest.integration"
//lazy val mhtmlV = "0.1.1"
//lazy val mhtmlV = "0.2.3"

val circeVersion = "0.7.0"


lazy val view = (project in file(".")).enablePlugins(ScalaJSPlugin)
  .settings(
    libraryDependencies ++= Seq(
      "in.nvilla" %%% "monadic-rx-cats" % mhtmlV,
      "in.nvilla" %%% "monadic-html" % mhtmlV,
      "com.lihaoyi" %%% "upickle" % "0.4.4",
      "fr.hmil" %%% "roshttp" % "2.0.1",
      "io.monix" %%% "monix" % "2.2.3"
    ),
    libraryDependencies ++= Seq(
      "io.circe" %%% "circe-core",
      "io.circe" %%% "circe-generic",
      "io.circe" %%% "circe-parser"
    ).map(_ % circeVersion)
  )
