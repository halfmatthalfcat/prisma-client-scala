lazy val root = (project in file("."))
  .enablePlugins(PrismaPlugin)
  .settings(
    name := "prisma-client-scala-postgres-default",
    logLevel := Level.Debug,
  )