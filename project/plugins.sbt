import sbt.Keys.sbtPlugin

import scala.collection.Seq

logLevel := Level.Debug

addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "3.9.4")

addSbtPlugin("com.jsuereth" % "sbt-pgp" % "2.0.1")

addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.13")

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "2.1.1")

addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.11.0")

/**
 * We need the below to incorporate the sbt-plugin into our
 * integration (db) projects
 *
 * @see https://stackoverflow.com/questions/37424513/intertwined-dependencies-between-sbt-plugin-and-projects-within-multi-project-bu/37513852#37513852
 */

lazy val root = (project in file(".")).dependsOn(`plugin-sbt`)

lazy val `plugin-generator` = (project in file("plugin-generator"))
  .enablePlugins(BuildInfoPlugin)
  .settings(
    scalaVersion := "2.13.10",
    Compile / run / mainClass := Some("com.github.halfmatthalfcat.Generator"),
    Compile / packageBin / mainClass := Some("com.github.halfmatthalfcat.Generator"),
    assembly / mainClass := Some("com.github.halfmatthalfcat.Generator"),
    Compile / unmanagedSourceDirectories :=
      mirrorScalaSource((ThisBuild / baseDirectory).value.getParentFile / "plugin-generator"),
    libraryDependencies ++= Seq(
      "com.github.plokhotnyuk.jsoniter-scala" %% "jsoniter-scala-core" % "2.21.3",
      "com.github.plokhotnyuk.jsoniter-scala" %% "jsoniter-scala-macros" % "2.21.3" % Provided,
      "org.scalameta" %% "munit" % "0.7.29" % Test,
      "org.scalameta" %% "scalameta" % "4.7.6",
    )
  )

lazy val `plugin-sbt` = (project in file("plugin-sbt"))
  .enablePlugins(SbtPlugin)
  .settings(
    scalaVersion := "2.12.16",
    sbtPlugin := true,
    Compile / unmanagedResources += (`plugin-generator` / assembly).value,
    Compile / unmanagedSourceDirectories :=
      mirrorScalaSource((ThisBuild / baseDirectory).value.getParentFile / "plugin-sbt"),
    libraryDependencies ++= Seq(
      "com.lihaoyi" %% "os-lib" % "0.9.1",
      "com.softwaremill.sttp.client3" %% "core" % "3.8.13",
      "com.softwaremill.sttp.client3" %% "jsoniter" % "3.8.13"
    )
  )

def mirrorScalaSource(baseDirectory: File): Seq[File] = {
  val scalaSourceDir = baseDirectory / "src" / "main" / "scala"
  if (scalaSourceDir.exists) scalaSourceDir :: Nil
  else sys.error(s"Missing source directory: $scalaSourceDir")
}