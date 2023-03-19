lazy val root = (project in file("."))
  .enablePlugins(PrismaPlugin)
  .settings(
    name := "prisma-client-scala-mssql-default",
    logLevel := Level.Debug,
  )