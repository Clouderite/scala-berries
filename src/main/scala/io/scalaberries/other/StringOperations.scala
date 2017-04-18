package io.scalaberries.other

import io.scalaberries.other.StringOperations.toStringOperations

class StringOperations(value: String) {
  def sliceLines(from: Int, to: Int): String = {
    require(from >= 0, "from parameter must be greater than or equal 0")
    require(to >= 0, "to parameter must be greater than or equal 0")
    require(from <= to, "from parameter must be lower than or equal to parameter")

    value.dropLines(from).takeLines(to - from + 1)
  }

  def negSliceLines(from: Int, to: Int): List[String] = {
    require(from >= 0, "from parameter must be greater than or equal 0")
    require(to >= 0, "to parameter must be greater than or equal 0")
    require(from <= to, "from parameter must be lower than or equal to parameter")
    
    List(value.takeLines(from), value.dropLines(to + 1))
  }

  def takeLines(num: Int): String = {
    require(num >= 0, "num parameter must be greater than or equal 0")
    value.linesWithSeparators.take(num).mkString
  }

  def dropLines(num: Int): String = {
    require(num >= 0, "num parameter must be greater than or equal 0")
    value.linesWithSeparators.drop(num).mkString
  }
}

object StringOperations {
  implicit def toStringOperations(value: String): StringOperations = new StringOperations(value)
}

