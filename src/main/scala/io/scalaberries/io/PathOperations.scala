package io.scalaberries.io

import java.nio.file.{Path, Paths}

import scala.annotation.tailrec

class PathOperations(path: Path) {
  val ExtensionSeparator = '.'
  val EmptyPath: Path = Paths.get("")

  def extension: Option[String] = {
    Option.apply(path.getFileName.toString)
      .map(name ⇒ (name, name.lastIndexOf(ExtensionSeparator)))
      .filter(_._2 > 0)
      .filter(t ⇒ t._1.length > t._2 + 1)
      .map(t ⇒ t._1.substring(t._2 + 1))
      .map(_.toLowerCase)
  }

  def mkdirs(): Unit = {
    val file = path.toFile
    file.mkdirs()
  }

  def prefixFileName(length: Int = 2, depth: Int = 2): Path = {
    require(length >= 0, depth >= 0)
    path.getParent.resolve(prefixedDirs(length, depth, EmptyPath)).resolve(path.getFileName)
  }

  @tailrec
  private def prefixedDirs(length: Int, depth: Int, subPath: Path): Path = {
    if (depth > 0) {
      val fileName = path.getFileName.toString
      val startIndex = (depth - 1) * length
      val endIndex = startIndex + length
      val newSubPath = Paths.get(fileName.substring(startIndex, endIndex)).resolve(subPath)
      prefixedDirs(length, depth - 1, newSubPath)
    } else {
      subPath
    }
  }
}

object PathOperations {
  implicit def toPathOperations(path: Path): PathOperations = {
    new PathOperations(path)
  }
}
