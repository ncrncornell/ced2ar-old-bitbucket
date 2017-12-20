import java.nio.file.{Files, StandardCopyOption}

name := "ced2ar3-view"

//TODO: set this from maven; also note setting sjs plugin version in plugins.sbt
scalaVersion in ThisBuild := "2.12.1" // or any other Scala version >= 2.10.2

lazy val mhtmlV = "0.4.0-RC1" //TODO: also set from mvn if possible

val circeVersion = "0.8.0"

lazy val copyCss = TaskKey[Unit]("copyCss")
val nodeModulesDir = "target/scala-2.12/scalajs-bundler/main/node_modules"
val cssInPaths = Map(
  "bootstrap/dist/css" -> "/css/",
  "bootstrap/dist/js" -> "/js/",
  "bootstrap/dist/fonts" -> "/fonts/",
  "jquery/dist" -> "/js/"
)
val apiSourceDir = "../services-core/src/main/java/edu/ncrn/cornell/service"
val sourceDirMap = Map("api" -> "/edu/ncrn/cornell/service/api/")
def copyFiles(
  base: File, trg: File, strms: TaskStreams,
  relSourceRoot: String, relPathMap: Map[String, String]
): Seq[File] = relPathMap.toSeq.map{csp => (new File(relSourceRoot, csp._1).toPath.toString, csp._2)}
  .flatMap{ case (relPathMap, relTarget) =>
    val outDirFiles: Array[File] = new File(base, relPathMap).listFiles().map { file =>
      val newFile = new File(new File(trg.toPath.toString, relTarget).getCanonicalFile, file.name)
      strms.log.info(
        s"copying ${file.toPath.toString} to ${newFile.toPath.toString}"
      )
      newFile.mkdirs()
      Files.copy(
        file.toPath, newFile.toPath,
        StandardCopyOption.REPLACE_EXISTING
      )
      newFile
    }
    outDirFiles
  }

lazy val view = (project in file("."))
  .enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)
  .settings(
    scalacOptions ++= Seq(
      "-feature"
      ,"-language:higherKinds"
    )
    // TODO: maybe later? requires system has yarn installed:
    ,useYarn := true
    // Execute the tests in browser-like environment:
    ,jsEnv in Test := new org.scalajs.jsenv.phantomjs.PhantomJSEnv()
    ,scalaJSUseMainModuleInitializer := true
    ,copyCss <<= (baseDirectory, target, streams) map {
      (base: File, trg: File, strms: TaskStreams) =>
        copyFiles(base, trg, strms, nodeModulesDir, cssInPaths)
    }
    ,sourceGenerators in Compile <+= (baseDirectory, sourceManaged in Compile, streams) map {
      (base: File, trg: File, strms: TaskStreams) =>
        copyFiles(base, trg, strms, apiSourceDir, sourceDirMap)
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
      "bootstrap" -> "3.3.7",
      "jquery" -> "3.2.1"
    )
  )
