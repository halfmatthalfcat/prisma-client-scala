package com.github.halfmatthalfcat.prisma.scala

sealed trait ScalaGenVersion

case object Scala2 extends ScalaGenVersion
case object Scala3 extends ScalaGenVersion
