package com.github.halfmatthalfcat.prisma.scala

import com.github.halfmatthalfcat.prisma.dmmf.{Field, FieldDefault, FieldDefaultKind, FieldKind, IsFieldDefault, Model, TypeKind}
import org.scalafmt.Scalafmt
import org.scalafmt.config.ScalafmtConfig

class ModelConversionSuite extends munit.FunSuite {
  test("produce a valid case class") {
    val model = Model(
      name = "Test",
      fields = Seq(
        Field(
          name = "id",
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
        ),
        Field(
          name = "email",
          kind = FieldKind.Scalar,
          `type` = TypeKind.String,
          isList = false,
          isRequired = true,
          isUnique = false,
          isId = false,
          isReadOnly = false,
          hasDefaultValue = false,
          default = Option.empty
        ),
      )
    )

    assertEquals(
      ModelConversion.getModel(model).syntax,
      "case class Test(id: java.util.UUID = java.util.UUID.randomUUID(), email: String)"
    )
  }
}
