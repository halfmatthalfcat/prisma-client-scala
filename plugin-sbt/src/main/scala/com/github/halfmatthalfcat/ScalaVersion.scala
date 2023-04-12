package com.github.halfmatthalfcat

sealed abstract class ScalaVersion(val version: Int)
object ScalaVersion {
  case object `2` extends ScalaVersion(2)

  case object `3` extends ScalaVersion(3)
}
