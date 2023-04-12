package com.github.halfmatthalfcat.prisma.scala

import scala.meta._
import com.github.halfmatthalfcat.prisma.dmmf.Model

object ModelConversion {
  def getModel(model: Model): Defn.Class = {
    val name = Type.Name(model.name)
    val fields = model.fields.map(FieldConversion.getField).toList
    q"case class $name (..$fields)"
  }
}
