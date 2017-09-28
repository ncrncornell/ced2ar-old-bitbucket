import java.nio.file.{Files, StandardCopyOption}

name := "ced2ar3-view"

//TODO: set this from maven; also note setting sjs plugin version in plugins.sbt
scalaVersion in ThisBuild := "2.12.1" // or any other Scala version >= 2.10.2

lazy val mhtmlV = "0.3.2" //TODO: also set from mvn if possible

val circeVersion = "0.8.0"

lazy val copyCss = TaskKey[Unit]("copyCss")
val nodeModulesDir = "target/scala-2.12/scalajs-bundler/main/node_modules"
val cssInPaths = List("bootstrap/dist/css")

lazy val view = (project in file("."))
  .enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)
  .settings(
    // TODO: maybe later? requires system has yarn installed:
    useYarn := true
    // Execute the tests in browser-like environment:
    ,requiresDOM in Test := true
    ,scalaJSUseMainModuleInitializer := true
    ,copyCss <<= (baseDirectory, target, streams) map {
      (base, trg, strms) =>
        cssInPaths.map{csp => new File(nodeModulesDir, csp).toPath.toString}.foreach{cssInPath =>
          new File(base, cssInPath).listFiles().foreach { file =>
            strms.log.info(
              s"copying ${file.toPath.toString} to ${trg.toPath.toString}"
            )
            Files.copy(
              file.toPath, new File(trg, file.name).toPath,
              StandardCopyOption.REPLACE_EXISTING
            )
          }
        }
    }
    ,webpackConfigFile in fullOptJS := Some(baseDirectory.value / "prod.webpack.config.js")
    ,webpackConfigFile in fastOptJS := Some(baseDirectory.value / "dev.webpack.config.js")
    ,libraryDependencies ++= Seq(
      "in.nvilla" %%% "monadic-rx-cats" % mhtmlV,
      "in.nvilla" %%% "monadic-html" % mhtmlV,
      "com.lihaoyi" %%% "upickle" % "0.4.4",
      "fr.hmil" %%% "roshttp" % "2.0.1",
      "io.monix" %%% "monix" % "2.2.3",
      "com.github.japgolly.scalacss" %%% "core" % "0.5.3"
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
