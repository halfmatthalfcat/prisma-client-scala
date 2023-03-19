/**
 * Prisma Scala Client SBT Build Configuration
 */

import ReleaseTransformations._

lazy val root = (project in file("."))
  .enablePlugins(SbtPlugin)
  .settings(
    name := "prisma-client-scala",
    organization := "com.github.halfmatthalfcat",
    scalaVersion := "2.12.16",
    sbtPlugin := true,
    scriptedLaunchOpts := {
      scriptedLaunchOpts.value ++
        Seq("-Xmx1024M", "-Dplugin.version=" + version.value)
    },
    scriptedBufferLog := false,
    crossVersion := CrossVersion.binary,
    crossScalaVersions := Seq(
      scalaVersion.value,
    ),
    pluginCrossBuild / sbtVersion := {
      scalaBinaryVersion.value match {
        case "2.10" => "0.13.18"
        case "2.12" => "1.8.2"
      }
    },
    libraryDependencies ++= Seq(
      "com.github.plokhotnyuk.jsoniter-scala" %% "jsoniter-scala-core" % "2.21.3",
      "com.github.plokhotnyuk.jsoniter-scala" %% "jsoniter-scala-macros" % "2.21.3" % Provided,
      "com.lihaoyi" %% "os-lib" % "0.9.1",
      "com.softwaremill.sttp.client3" %% "core" % "3.8.13",
      "com.softwaremill.sttp.client3" %% "jsoniter" % "3.8.13",
    ),
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
