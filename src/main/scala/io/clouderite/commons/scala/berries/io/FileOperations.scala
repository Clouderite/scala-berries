package io.clouderite.commons.scala.berries.io

import java.nio.file.{CopyOption, Files, Path}

trait FileOperations {
  def readAllBytes(path: Path): Array[Byte]
  def copy(sourcePath: Path, destinationPath: Path, options: CopyOption)
  def delete(path: Path)
}

object FileOperations extends FileOperations {
  override def readAllBytes(path: Path): Array[Byte] = Files.readAllBytes(path)
  override def copy(sourcePath: Path, destinationPath: Path, options: CopyOption): Unit = Files.copy(sourcePath, destinationPath, options)
  override def delete(path: Path): Unit = Files.delete(path)
}