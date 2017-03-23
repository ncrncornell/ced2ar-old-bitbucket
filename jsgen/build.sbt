import java.nio.file.Files

name := "ced2ar3-view"

//TODO: set this from maven; also note setting sjs plugin version in plugins.sbt
scalaVersion in ThisBuild := "2.12.1" // or any other Scala version >= 2.10.2

lazy val mhtmlV = "0.3.0-RC1" //TODO: also set from mvn if possible

val circeVersion = "0.7.0"

lazy val copyCss = TaskKey[Unit]("copyCss")
// FIXME; need: copyCss dependsOn webpack
val cssInPath = "target/scala-2.12/scalajs-bundler/main/node_modules/bootstrap/dist/css"
val cssOutPath = "scala-2.12/scalajs-bundler"


lazy val view = (project in file("."))
  .enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)
  .settings(
    // TODO: maybe later? requires system has yarn installed:
    // useYarn := true
    // Execute the tests in browser-like environment:
    requiresDOM in Test := true
    ,copyCss <<= (baseDirectory, target, streams) map {
      (base, trg, strms) =>
        val absOutPath: File = new File(trg, cssOutPath)
        new File(base, cssInPath).listFiles().foreach { file =>
          strms.log.info(
            s"copying ${file.toPath.toString} to ${absOutPath.toPath.toString}"
          )
          Files.copy(file.toPath, new File(absOutPath, file.name).toPath)
        }
    }
    ,webpackConfigFile in fullOptJS := Some(baseDirectory.value / "prod.webpack.config.js")
    ,webpackConfigFile in fastOptJS := Some(baseDirectory.value / "dev.webpack.config.js")
    ,libraryDependencies ++= Seq(
      "in.nvilla" %%% "monadic-rx-cats" % mhtmlV,
      "in.nvilla" %%% "monadic-html" % mhtmlV,
      "com.lihaoyi" %%% "upickle" % "0.4.4",
      "fr.hmil" %%% "roshttp" % "2.0.1",
      "io.monix" %%% "monix" % "2.2.3"
    )
    ,libraryDependencies ++= Seq(
      "io.circe" %%% "circe-core",
      "io.circe" %%% "circe-generic",
      "io.circe" %%% "circe-parser"
    ).map(_ % circeVersion)
    ,npmDependencies in Compile ++= Seq(
      "bootstrap" -> "3.3.7"
    )
  )
