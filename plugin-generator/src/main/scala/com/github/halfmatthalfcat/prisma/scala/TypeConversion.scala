package com.github.halfmatthalfcat.prisma.scala

import com.github.halfmatthalfcat.prisma.dmmf.TypeKind

import scala.meta._

object TypeConversion {
  def typeKindToType(kind: TypeKind): Type = kind match {
    case TypeKind.Boolean => t"Boolean"
    case TypeKind.String | TypeKind.ID => t"String"
    case TypeKind.Int => t"Int"
    case TypeKind.BigInt => t"scala.math.BigInt"
    case TypeKind.Long => t"Long"
    case TypeKind.Float => t"Float"
    case TypeKind.Decimal => t"Double"
    case TypeKind.UUID => t"java.util.UUID"
    case TypeKind.Bytes => t"Seq[Byte]"
    case TypeKind.DateTime => t"java.time.ZonedDateTime"
    case TypeKind.Json => t"com.github.halfmatthalfcat.scala.Json"
    case TypeKind.Other(kind) => t"$kind"
  }
}
