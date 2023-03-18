/**
 * Prisma Scala Client SBT Build Configuration
 */

import ReleaseTransformations._

lazy val root = (project in file("."))
  .settings(
    name := "prisma-client-scala",
    organization := "com.github.halfmatthalfcat",
    scalaVersion := "2.12.16",
    sbtPlugin := true,
    crossVersion := CrossVersion.binary,
    crossScalaVersions := Seq(
      scalaVersion.value,
    ),
    pluginCrossBuild / sbtVersion := {
      scalaBinaryVersion.value match {
        case "2.10" => "0.13.18"
        case "2.12" => "1.3.10"
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

addSbtPlugin("com.typesafe.sbt" % "sbt-js-engine" % "1.2.3")