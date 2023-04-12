package com.github.halfmatthalfcat.prisma.scala

import com.github.halfmatthalfcat.prisma.dmmf.{Field, FieldDefault, FieldDefaultKind, FieldDefaultValue, FieldKind, IsFieldDefault, TypeKind}

class FieldConversionSuite extends munit.FunSuite {
  test("getFieldDefault should return randomUUID") {
    val field = Field(
      name = "test",
      kind = FieldKind.Scalar,
      `type` = TypeKind.String,
      isList = false,
      isRequired = true,
      isUnique = false,
      isId = false,
      isReadOnly = false,
      hasDefaultValue = true,
      default = Some(IsFieldDefault(FieldDefault(
        kind = FieldDefaultKind.UUID,
        args = Seq.empty,
      )))
    )

    assert {
      FieldConversion.getFieldDefault(field).map(_.syntax).contains {
        "java.util.UUID.randomUUID()"
      }
    }
  }

  test("getFieldDefault should return getUlid") {
    val field = Field(
      name = "test",
      kind = FieldKind.Scalar,
      `type` = TypeKind.String,
      isList = false,
      isRequired = true,
      isUnique = false,
      isId = false,
      isReadOnly = false,
      hasDefaultValue = true,
      default = Some(IsFieldDefault(FieldDefault(
        kind = FieldDefaultKind.CUID,
        args = Seq.empty,
      )))
    )

    assert {
      FieldConversion.getFieldDefault(field).map(_.syntax).contains {
        "com.github.f4b6a3.ulid.UlidCreator.getUlid()"
      }
    }
  }

  test("getFieldDefault should return empty") {
    val field = Field(
      name = "test",
      kind = FieldKind.Scalar,
      `type` = TypeKind.String,
      isList = false,
      isRequired = true,
      isUnique = false,
      isId = false,
      isReadOnly = false,
      hasDefaultValue = false,
      default = Option.empty
    )

    assert {
      FieldConversion.getFieldDefault(field).isEmpty
    }
  }

  test("getFieldType should return a type") {
    val field = Field(
      name = "test",
      kind = FieldKind.Scalar,
      `type` = TypeKind.String,
      isList = false,
      isRequired = true,
      isUnique = false,
      isId = false,
      isReadOnly = false,
      hasDefaultValue = false,
      default = Option.empty
    )

    assertEquals(
      FieldConversion.getFieldType(field).syntax,
      "String"
    )
  }

  test("getFieldType should return a Seq of type") {
    val field = Field(
      name = "test",
      kind = FieldKind.Scalar,
      `type` = TypeKind.String,
      isList = true,
      isRequired = true,
      isUnique = false,
      isId = false,
      isReadOnly = false,
      hasDefaultValue = false,
      default = Option.empty
    )

    assertEquals(
      FieldConversion.getFieldType(field).syntax,
      "Seq[String]"
    )
  }

  test("getFieldType should return an Option of type") {
    val field = Field(
      name = "test",
      kind = FieldKind.Scalar,
      `type` = TypeKind.String,
      isList = false,
      isRequired = false,
      isUnique = false,
      isId = false,
      isReadOnly = false,
      hasDefaultValue = false,
      default = Option.empty
    )

    assertEquals(
      FieldConversion.getFieldType(field).syntax,
      "Option[String]"
    )
  }

  test("getFieldType should return a Seq Option of type") {
    val field = Field(
      name = "test",
      kind = FieldKind.Scalar,
      `type` = TypeKind.String,
      isList = true,
      isRequired = false,
      isUnique = false,
      isId = false,
      isReadOnly = false,
      hasDefaultValue = false,
      default = Option.empty
    )

    assertEquals(
      FieldConversion.getFieldType(field).syntax,
      "Option[Seq[String]]"
    )
  }

  test("getField should return a field without default") {
    val field = Field(
      name = "test",
      kind = FieldKind.Scalar,
      `type` = TypeKind.String,
      isList = false,
      isRequired = true,
      isUnique = false,
      isId = false,
      isReadOnly = false,
      hasDefaultValue = false,
      default = Option.empty
    )

    assertEquals(
      FieldConversion.getField(field).syntax,
      "test: String"
    )
  }

  test("getField should return a field with default") {
    val field = Field(
      name = "test",
      kind = FieldKind.Scalar,
      `type` = TypeKind.UUID,
      isList = false,
      isRequired = true,
      isUnique = false,
      isId = false,
      isReadOnly = false,
      hasDefaultValue = true,
      default = Some(IsFieldDefault(FieldDefault(
        kind = FieldDefaultKind.UUID,
        args = Seq.empty,
      )))
    )

    assertEquals(
      FieldConversion.getField(field).syntax,
      "test: java.util.UUID = java.util.UUID.randomUUID()"
    )
  }
}
