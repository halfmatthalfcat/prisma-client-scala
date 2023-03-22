logLevel := Level.Debug

addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "3.9.4")

addSbtPlugin("com.jsuereth" % "sbt-pgp" % "2.0.1")

addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.13")

/**
 * We need the below to incorporate the sbt-plugin into our
 * integration (db) projects
 */

lazy val `plugin-generator` = ProjectRef(file("../plugin-generator"), "plugin-generator")

lazy val `plugin-sbt` = ProjectRef(file("../plugin-sbt"), "plugin-sbt")

lazy val root = (project in file("."))
  .dependsOn(`plugin-sbt`, `plugin-generator`)
  .settings(
    `plugin-sbt` / scalaVersion := "2.12.16",
    `plugin-generator` / scalaVersion := "2.12.16",
    `plugin-sbt` / Compile / unmanagedJars += (`plugin-generator` / Compile / packageBin).value,
  )
