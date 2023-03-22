/**
 * Prisma Scala Client SBT Build Configuration
 */

import ReleaseTransformations._

val commonName = "prisma-client-scala"
val commonSettings = Seq(
  organization := "com.github.halfmatthalfcat",
  scalaVersion := "2.12.16",
)

lazy val root = (project in file("."))
  .settings(name := commonName)

lazy val `plugin-generator` = (project in file("plugin-generator"))
  .settings(commonSettings: _*)
  .settings(
    name := s"$commonName-plugin-generator"
  )

lazy val `plugin-sbt` = (project in file("plugin-sbt"))
  .settings(commonSettings: _*)
  .settings(
    name := s"$commonName-plugin-sbt",
    Compile / unmanagedJars += (`plugin-generator` / Compile / packageBin).value,
    scriptedLaunchOpts := {
      scriptedLaunchOpts.value ++
        Seq("-Xmx1024M", "-Dplugin.version=" + version.value)
    },
    scriptedBufferLog := false,
    crossVersion := CrossVersion.binary,
      crossScalaVersions := Seq (
      scalaVersion.value
    ),
    pluginCrossBuild / sbtVersion := {
      scalaBinaryVersion.value match {
        case "2.10" => "0.13.18"
        case "2.12" => "1.8.2"
      }
    },
    releaseCrossBuild := true,
    releaseProcess := Seq[ReleaseStep](
      checkSnapshotDependencies,
      inquireVersions,
      runClean,
      runTest,
      setReleaseVersion,
      commitReleaseVersion,
      tagRelease,
      releaseStepCommandAndRemaining("+publishSigned"),
      releaseStepCommand("sonatypeBundleRelease"),
      setNextVersion,
      commitNextVersion,
      pushChanges
    ),
    pomExtra :=
      <url>https://www.github.com/halfmatthalfcat/prisma-scala-client</url>
        <licenses>
          <license>
            <name>MIT</name>
            <distribution>repo</distribution>
          </license>
        </licenses>
        <scm>
          <url>git@github.com:halfmatthalfcat/prisma-scala-client.git</url>
          <connection>scm:git:git@github.com:halfmatthalfcat/prisma-scala-client.git</connection>
        </scm>
        <developers>
          <developer>
            <id>halfmatthalfcat</id>
            <name>Matt Oliver</name>
            <url>https://www.github.com/halfmatthalfcat</url>
          </developer>
        </developers>,
    publishMavenStyle := true,
    publishTo := sonatypePublishToBundle.value,
    resolvers ++= Seq(DefaultMavenRepository)
  )

lazy val sqlite = (project in file("sqlite"))
  .enablePlugins(PrismaPlugin)
  .settings(commonSettings: _*)
  .settings(
    name := s"$commonName-sqlite"
  )

lazy val postgres = (project in file("postgres"))
  .enablePlugins(PrismaPlugin)
  .settings(commonSettings: _*)
  .settings(
    name := s"$commonName-postgres"
  )

lazy val mssql = (project in file("mssql"))
  .enablePlugins(PrismaPlugin)
  .settings(commonSettings: _*)
  .settings(
    name := s"$commonName-mssql"
  )

lazy val mongodb = (project in file("mongodb"))
  .enablePlugins(PrismaPlugin)
  .settings(commonSettings: _*)
  .settings(
    name := s"$commonName-mongodb"
  )
