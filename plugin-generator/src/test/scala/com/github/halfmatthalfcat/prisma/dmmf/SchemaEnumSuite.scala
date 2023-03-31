package com.github.halfmatthalfcat.prisma.dmmf

class SchemaEnumSuite extends munit.FunSuite {
  test("produce a valid Scala 2 enum") {
    val schemaEnum = SchemaEnum("Test", Seq("A", "B", "C"))

    assertEquals(
      schemaEnum.scala2().syntax,
      """object Test extends Enumeration {
        |  type Test = Value
        |  val A, B, C = Value
        |}""".stripMargin
    )
  }

  test("produce a valid Scala 2 enum codec") {
    val schemaEnum = SchemaEnum("Test", Seq("A", "B", "C"))

    assertEquals(
      schemaEnum.scala2Codec().get.syntax,
      """implicit val codec: JsonValueCodec[Test] = new JsonValueCodec[Test] {
        |  override def decodeValue(in: JsonReader, default: Test): Test = {
        |    val b = in.nextToken()
        |    if (b == '"') {
        |      in.rollbackToken()
        |      val str = in.readString(null)
        |      Test.values.find(_.toString == str).getOrElse(in.enumValueError("expected " + "Test"))
        |    } else in.decodeError("expected " + "Test")
        |  }
        |  override def encodeValue(x: Test, out: JsonWriter): Unit = out.writeVal(x.toString)
        |  override def nullValue: Test = null.asInstanceOf[Test]
        |}""".stripMargin
    )
  }

  test("produce a valid Scala 3 enum") {
    val schemaEnum = SchemaEnum("Test", Seq("A", "B", "C"))

    assertEquals(
      schemaEnum.scala3().syntax,
      """enum Test { case A, B, C }"""
    )
  }

  test("produce a valid Scala 3 enum codec") {
    val schemaEnum = SchemaEnum("Test", Seq("A", "B", "C"))

    assertEquals(
      schemaEnum.scala3Codec().get.syntax,
      """given JsonValueCodec[Test] = new JsonValueCodec[Test] {
        |  override def decodeValue(in: JsonReader, default: Test): Test = {
        |    val b = in.nextToken()
        |    if (b == '"') {
        |      in.rollbackToken()
        |      val str = in.readString(null)
        |      Test.values.find(_.toString == str).getOrElse(in.enumValueError("expected " + "Test"))
        |    } else in.decodeError("expected " + "Test")
        |  }
        |  override def encodeValue(x: Test, out: JsonWriter): Unit = out.writeVal(x.toString)
        |  override def nullValue: Test = null.asInstanceOf[Test]
        |}""".stripMargin
    )
  }
}
