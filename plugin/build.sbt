enablePlugins(SbtPlugin)
sbtPlugin := true
scriptedLaunchOpts := {
  scriptedLaunchOpts.value ++
    Seq("-Xmx1024M", "-Dplugin.version=" + version.value)
}
scriptedBufferLog := false

libraryDependencies ++= Seq(
  "com.github.plokhotnyuk.jsoniter-scala" %% "jsoniter-scala-core" % "2.21.3",
  "com.github.plokhotnyuk.jsoniter-scala" %% "jsoniter-scala-macros" % "2.21.3" % Provided,
  "com.lihaoyi" %% "os-lib" % "0.9.1",
  "com.softwaremill.sttp.client3" %% "core" % "3.8.13",
  "com.softwaremill.sttp.client3" %% "jsoniter" % "3.8.13"
)