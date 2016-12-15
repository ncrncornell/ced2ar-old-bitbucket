name := "ced2ar3-view"

scalaVersion in ThisBuild := "2.11.8" // or any other Scala version >= 2.10.2

lazy val mhtmlV = "latest.integration"
//lazy val mhtmlV = "0.1.1"



//artifactPath in (Compile, packageScalaJSLauncher) :=
//  (resourceManaged in Compile).value / "public"
////configure a specific directory for scalajs output

//val scalajsOutputDir = Def.settingKey[File]("directory for javascript files output by scalajs")


lazy val view = (project in file(".")).enablePlugins(ScalaJSPlugin)
  .settings(
    libraryDependencies ++= Seq(
      "in.nvilla" %%% "monadic-rx-cats" % mhtmlV,
      "in.nvilla" %%% "monadic-html" % mhtmlV,
      "com.lihaoyi" %%% "upickle" % "0.3.9"
    )
  )

//From http://www.mikaelmayer.com/2015/08/06/integrating-scala-js-into-an-existing-project/
//FIXME: runs but doesn't appear to copy
lazy val copyDyn = Def.taskDyn{
  val outDir = baseDirectory.value / "target/classes/public"
  val inDir = baseDirectory.value / "target/scala-2.11"
  val jsFiles = baseDirectory (_ * "*.js" get)
  Def.task(jsFiles.value map { p =>  (inDir / p.getName, outDir / p.getName) })
}
lazy val copyjs = TaskKey[Unit]("copyjs", "Copy javascript files to target directory")
copyjs := {
  val files = copyDyn.value
  IO.copy(files, true)

}