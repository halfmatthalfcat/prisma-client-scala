/**
 * The SBT "bare" configuration is generally frowned upon at this point (https://github.com/sbt/sbt/issues/6217)
 * but since we want to leverage this configuration for both the root build and the meta build, this is the best
 * way I've found to share these settings with both.
 */

enablePlugins(SbtPlugin)

sbtPlugin := true
libraryDependencies ++= Seq(
  "com.github.plokhotnyuk.jsoniter-scala" %% "jsoniter-scala-core" % "2.21.3",
  "com.github.plokhotnyuk.jsoniter-scala" %% "jsoniter-scala-macros" % "2.21.3" % Provided,
  "com.lihaoyi" %% "os-lib" % "0.9.1",
  "com.softwaremill.sttp.client3" %% "core" % "3.8.13",
  "com.softwaremill.sttp.client3" %% "jsoniter" % "3.8.13"
)
