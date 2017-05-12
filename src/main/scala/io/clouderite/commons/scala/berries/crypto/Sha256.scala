package io.clouderite.commons.scala.berries.crypto

import java.nio.file.Path
import java.security.MessageDigest

import io.clouderite.commons.scala.berries.io.FileOperations

object Sha256 {
  private val digest = MessageDigest.getInstance("SHA-256")

  def hashFile(path: Path)(implicit fileOperations: FileOperations): Array[Byte] = {
    val data = fileOperations.readAllBytes(path)
    hash(data)
  }

  def hash(data: Array[Byte]): Array[Byte] = {
    digest.update(data)
    digest.digest()
  }
}
