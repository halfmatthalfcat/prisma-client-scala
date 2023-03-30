package com.github.halfmatthalfcat.prisma.dmmf

import com.github.plokhotnyuk.jsoniter_scala.core.{JsonReader, JsonValueCodec, JsonWriter}

object FieldNamespace extends Enumeration {
  type FieldNamespace = Value
  
  protected case class FieldNamespaceVal(namespace: String) extends super.Val
  implicit def valueToFieldNamespaceVal(x: Value): FieldNamespaceVal = x.asInstanceOf[FieldNamespaceVal]
  implicit val codec: JsonValueCodec[FieldNamespace] = new JsonValueCodec[FieldNamespace] {
    override def decodeValue(in: JsonReader, default: FieldNamespace): FieldNamespace = {
      val b = in.nextToken()
      if (b == '"') {
        in.rollbackToken()
        val str = in.readString(null)
        FieldNamespace.values.find(_.namespace == str).getOrElse(
          in.enumValueError("expected FieldNamespace")
        )
      } else in.decodeError("expected FieldNamespace")
    }

    override def encodeValue(x: FieldNamespace, out: JsonWriter): Unit =
      out.writeVal(x.namespace)

    override def nullValue: FieldNamespace = null.asInstanceOf[FieldNamespace]
  }
  
  val Model: FieldNamespaceVal = FieldNamespaceVal("model")
  val Prisma: FieldNamespaceVal = FieldNamespaceVal("prisma")
}
