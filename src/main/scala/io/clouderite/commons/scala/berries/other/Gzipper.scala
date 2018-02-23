package io.clouderite.commons.scala.berries.other

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}
import java.util.zip.{GZIPInputStream, GZIPOutputStream}

import scala.io.Source

object Gzipper {
  def gzip(value: String): Array[Byte] = {
    val b = new ByteArrayOutputStream()
    val gzip = new GZIPOutputStream(b)
    gzip.write(value.getBytes("UTF-8"))
    gzip.close()
    b.toByteArray
  }

  def gunzip(value: Array[Byte]): String = {
    val b = new ByteArrayInputStream(value)
    val gunzip = new GZIPInputStream(b)
    Source.fromInputStream(gunzip).mkString
  }
}
