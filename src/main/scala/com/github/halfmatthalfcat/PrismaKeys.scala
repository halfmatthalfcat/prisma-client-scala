package com.github.halfmatthalfcat

import sbt.*

trait PrismaKeys {
  lazy val prismaOutDir = settingKey[String]("Directory to emit files")
  lazy val prismaSchemaLocation = settingKey[String]("Location of the schema.prisma file")

  lazy val prismaGenerate = taskKey[Unit]("Generate the Scala Prisma client")
  lazy val prismaFormat = taskKey[Unit]("Format the Prisma file")
  lazy val prismaMigrate = taskKey[Unit]("Run Prisma migration")
}
