import sbt.util

logLevel := Level.Debug

addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "3.9.4")

addSbtPlugin("com.jsuereth" % "sbt-pgp" % "2.0.1")

addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.13")

/**
 * We need the below to incorporate the plugin into our
 * integration (db) projects
 */

lazy val pluginProject = ProjectRef(file("../plugin"), "plugin")

lazy val root = (project in file("."))
  .settings(logLevel := util.Level.Debug)
  .dependsOn(pluginProject)
