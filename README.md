<div align="center">
    <h1>Prisma Client Scala</h1>
    <p><b>The Prisma ORM on Scala</b></p>
</div>

> This project is currently a work in progress and in an unstable state. Once we reach GA, we'll remove
this banner and begin publishing stable versions to Maven.

* Generate typesafe database queries and models in Scala
* Manage database migrations seamlessly
* Leverage the existing Prisma specification and ecosystem

## High Level Overview

`Prisma Client Scala` is a Scala interop library for projects wanting to leverage the [Prisma](https://github.com/prisma/prisma)
database "ORM-lite" project. This project provides an SBT plugin that drives many of the core Prisma
functions (formatting, migrations, compile-time client generation).

At it's core, Prisma is a [database schema specification](https://www.prisma.io/docs/reference/api-reference/prisma-schema-reference) (schema.prisma) that
drives a codegen process. This codegen process emits a [database client](https://www.prisma.io/docs/reference/api-reference/prisma-client-reference) specific to the target database and schema
that manages the database connection and provides typesafe models for both the queries and results.
