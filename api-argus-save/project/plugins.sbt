logLevel := Level.Warn

resolvers += Resolver.sonatypeRepo("snapshots")

addSbtPlugin("io.get-coursier" % "sbt-coursier" % "1.0.0-M15")


addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.14")

// Holding off on Scala-native support for now / until needed:

//val pluginVersion = sys.props.get("plugin.version").getOrElse("0.1.0-SNAPSHOT")
//addSbtPlugin("org.scala-native" % "sbt-scalajs-cross" % pluginVersion)
//addSbtPlugin("org.scala-native" % "sbt-cross"         % pluginVersion)
//addSbtPlugin("org.scala-native" % "sbt-scala-native"  % "0.1.0-SNAPSHOT")
//
//scalacOptions ++= Seq(
//  "-deprecation",
//  "-unchecked",
//  "-feature",
//  "-encoding",
//  "utf8"
//)
