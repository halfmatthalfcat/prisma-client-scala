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
  .enablePlugins(SbtPlugin)
  .settings(name := commonName)

lazy val plugin = (project in file("plugin"))
  .settings(commonSettings: _*)
  .settings(
    name := s"$commonName-plugin",
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
