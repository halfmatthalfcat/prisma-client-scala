package com.github.halfmatthalfcat

import sbt._

trait PrismaKeys {
  lazy val prismaOutDir = settingKey[File]("Directory to emit files")
  lazy val prismaSchemaFile = settingKey[File]("Location of the schema.prisma file")
  lazy val prismaScalaVersion = settingKey[ScalaVersion]("Scala version to emit")

  lazy val prisma = inputKey[Unit]("Invoke a Prisma command")
}
