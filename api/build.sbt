name := "ced2ar3- api"

version := "0.0.1-SNAPSHOT"

scalaVersion in ThisBuild := "2.12.1"

val circeVersion = "0.7.0"

lazy val root = project.in(file(".")).
  aggregate(apiJS, apiJVM).
  settings(
    publish := {},
    publishLocal := {}
  )

lazy val api = crossProject.in(file("."))
  //.settings(scalaVersion := "2.11.8")
  .settings(
    ivyConfigurations ++= Seq(config("compileonly").hide),
    unmanagedClasspath in Compile ++=
      update.value.select(configurationFilter("compileonly")),
    libraryDependencies ++= Seq(
      "com.lihaoyi" %%% "upickle" % "0.4.4",
      "com.github.aishfenton" %% "argus" % "0.2.7" % "compileonly"
    ),
    libraryDependencies ++= Seq(
      "io.circe" %%% "circe-core",
      "io.circe" %%% "circe-generic",
      "io.circe" %%% "circe-parser"
    ).map(_ % circeVersion)
  )

lazy val apiJS     = api.js
lazy val apiJVM    = api.jvm

