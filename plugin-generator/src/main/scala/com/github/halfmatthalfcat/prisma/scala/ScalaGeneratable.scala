package com.github.halfmatthalfcat.prisma.scala

import scala.meta.Defn

trait ScalaGeneratable {

  val genImport: Seq[ScalaImport.Import] = Seq.empty

  // The actual Scala2 source
  def scala2(): Defn

  // The Scala2 jsoniter (de)serialization codec
  def scala2Codec(): Option[Defn] = Option.empty

  // The actual Scala3 source
  def scala3(): Defn

  // The Scala3 jsoniter (de)serialization codec
  def scala3Codec(): Option[Defn] = Option.empty
}
