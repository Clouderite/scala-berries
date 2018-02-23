package io.clouderite.commons.scala.berries.io

import java.nio.file.{Path, Paths}

object PathConversions {
  implicit def toPath(value: String): Path =
    Paths.get(value)

  implicit def toString(value: Path): String =
    value.toString
}
