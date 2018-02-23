package io.clouderite.commons.scala.berries.io

import java.io.{IOException, InputStream}
import java.nio.file.{Files, Path, Paths}

import io.clouderite.commons.scala.berries.crypto.HashOperations.toHashOperations
import io.clouderite.commons.scala.berries.crypto.Sha256
import io.clouderite.commons.scala.berries.io.PathOperations.toPathOperations

import scala.annotation.tailrec
import scala.io.Source
import scala.util.Try

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

  def length: Int = {
    path.toString.length
  }

  def base: Path = {
    val pathLength = path.length
    val extensionLength = path.extension.map(e ⇒ e.length).getOrElse(0)
    Paths.get(path.toString.substring(0, pathLength - extensionLength - 1))
  }

  def mkdirs(): Unit = {
    val file = path.toFile
    file.mkdirs()
  }

  def prefixFileName(length: Int = 2, depth: Int = 2): Path = {
    require(length >= 0, depth >= 0)
    require(path.base.length >= length * depth)
    val parent = Option.apply(path.getParent)
    val child = prefixedDirs(length, depth, EmptyPath).resolve(path.getFileName)
    parent.map(p ⇒ p.resolve(child)).getOrElse(child)
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

  def hash(implicit fileOperations: FileOperations): String = {
    Sha256.hashFile(path).toHex
  }

  def withInputStream[T](inner: (InputStream) ⇒ T): Try[T] = {
    val is = Files.newInputStream(path)
    val tryResult = Try(inner(is))
    is.close()
    tryResult
  }

  def readText: String =
    Source.fromFile(path.toFile).mkString

  def readBinary: Array[Byte] =
    Source.fromFile(path.toFile).map(_.toByte).toArray

  def readResourceText: String = {
    def withResourceInputStream[T](inner: InputStream ⇒ T): T = {
      val classLoader = Thread.currentThread().getContextClassLoader
      val is = Try(classLoader.getResourceAsStream(path.toString))
      val res = is.map(inner).getOrElse(throw new IOException())
      is.map(_.close())
      res
    }

    withResourceInputStream { is ⇒
      Source.fromInputStream(is).mkString
    }
  }
}

object PathOperations {
  implicit def toPathOperations(path: Path): PathOperations = {
    new PathOperations(path)
  }
}
