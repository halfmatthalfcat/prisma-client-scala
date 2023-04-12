/**
 * Prisma Scala Client SBT Build Configuration
 */

import ReleaseTransformations._

val commonName = "prisma-client-scala"
val commonOrganization = "com.github.halfmatthalfcat"

lazy val root = (project in file("."))
  .settings(name := commonName)
  .aggregate(
    `plugin-generator`,
    `plugin-sbt`
  )

lazy val `plugin-generator` = (project in file("plugin-generator"))
  .enablePlugins(BuildInfoPlugin, AssemblyPlugin)
  .settings(
    name := s"$commonName-plugin-generator",
    organization := commonOrganization,
    scalaVersion := "2.13.10",
    Compile / run / mainClass := Some("com.github.halfmatthalfcat.Generator"),
    Compile / packageBin / mainClass := Some("com.github.halfmatthalfcat.Generator"),
    assembly / mainClass := Some("com.github.halfmatthalfcat.Generator"),
    assembly / assemblyJarName := s"generator.jar",
    libraryDependencies ++= Seq(
      "com.github.plokhotnyuk.jsoniter-scala" %% "jsoniter-scala-core" % "2.21.3",
      "com.github.plokhotnyuk.jsoniter-scala" %% "jsoniter-scala-macros" % "2.21.3" % Provided,
      "com.github.scopt" %% "scopt" % "4.1.0",
      "com.lihaoyi" %% "os-lib" % "0.7.7",
      "org.scalameta" %% "munit" % "0.7.29" % Test,
      "org.scalameta" %% "scalameta" % "4.7.6",
      "org.scalameta" %% "scalafmt-core" % "3.7.3"
    )
  )

lazy val `plugin-sbt` = (project in file("plugin-sbt"))
  .enablePlugins(SbtPlugin)
  .settings(
    name := s"$commonName-plugin-sbt",
    organization := commonOrganization,
    scalaVersion := "2.12.16",
    Compile / managedResources += (`plugin-generator` / assembly).value,
    libraryDependencies ++= Seq(
      "com.lihaoyi" %% "os-lib" % "0.9.1",
      "com.softwaremill.sttp.client3" %% "core" % "3.8.13",
      "com.softwaremill.sttp.client3" %% "jsoniter" % "3.8.13"
    ),
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
  .settings(
    name := s"$commonName-sqlite",
    organization := commonOrganization,
    scalaVersion := "3.2.2",
  )

lazy val postgres = (project in file("postgres"))
  .enablePlugins(PrismaPlugin)
  .settings(
    name := s"$commonName-postgres",
    organization := commonOrganization,
    scalaVersion := "3.2.2",
  )

lazy val mssql = (project in file("mssql"))
  .enablePlugins(PrismaPlugin)
  .settings(
    name := s"$commonName-mssql",
    organization := commonOrganization,
    scalaVersion := "3.2.2",
  )

lazy val mongodb = (project in file("mongodb"))
  .enablePlugins(PrismaPlugin)
  .settings(
    name := s"$commonName-mongodb",
    organization := commonOrganization,
    scalaVersion := "3.2.2",
  )
