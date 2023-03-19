lazy val root = (project in file("."))
  .enablePlugins(PrismaPlugin)
  .settings(
    name := "prisma-client-scala-sqlite-default",
    logLevel := Level.Debug,
  )