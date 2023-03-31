package com.github.halfmatthalfcat.prisma.scala

import scala.meta._

trait ScalaGeneratable {

  // Imports needed for Scala 2 gen
  val scala2Import: Seq[ScalaImport.Import] = Seq.empty

  // The actual Scala2 source
  def scala2(withCodec: Boolean): Tree

  // The Scala2 jsoniter (de)serialization codec
  def scala2Codec(): Option[Defn] = Option.empty

  // Imports needed for Scala 3 gen
  val scala3Import: Seq[ScalaImport.Import] = Seq.empty

  // The actual Scala3 source
  def scala3(withCodec: Boolean): Tree

  // The Scala3 jsoniter (de)serialization codec
  def scala3Codec(): Option[Defn] = Option.empty
}
