package com.github.halfmatthalfcat

import sbt._

trait PrismaKeys {
  lazy val prismaOutDir = settingKey[String]("Directory to emit files")
  lazy val prismaSchemaLocation = settingKey[String]("Location of the schema.prisma file")

  lazy val prisma = inputKey[Unit]("Invoke a Prisma command")
}
