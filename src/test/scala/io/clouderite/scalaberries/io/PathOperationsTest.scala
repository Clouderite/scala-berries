package io.clouderite.scalaberries.io

import java.nio.file.Paths

import io.clouderite.scalaberries.io.PathOperations.toPathOperations
import org.scalacheck.Gen
import org.scalacheck.Gen.alphaChar
import org.scalatest.prop.{GeneratorDrivenPropertyChecks, TableDrivenPropertyChecks}
import org.scalatest.{FlatSpec, GivenWhenThen, MustMatchers}

class PathOperationsTest extends FlatSpec with MustMatchers with GeneratorDrivenPropertyChecks with TableDrivenPropertyChecks with GivenWhenThen {

  "extension" should "return last part of filename separated by dot" in {
    forAll(Gen.alphaStr, Gen.alphaStr) {
      (name, extension) ⇒
        whenever(name.length > 0 && extension.length > 0) {
          Given(s"file name and extension - $name, $extension")
          val file = Paths.get(name + "." + extension)
          val expectedExtension = extension.toLowerCase

          When("extension is called")
          val retExtension = file.extension

          Then("returned extension must match expected one")
          retExtension.get mustBe expectedExtension
        }
    }
  }

  "extension" should "return empty when extension is empty string" in {
    forAll(Gen.alphaStr) {
      (name) ⇒
        whenever(name.length > 0) {
          Given(s"file name and empty extension - $name")
          val file = Paths.get(name + ".")

          When("extension is called")
          val retExtension = file.extension

          Then("returned extension must be empty")
          retExtension mustBe None
        }
    }
  }

  "extension" should "return lower-cased extension" in {
    forAll(Gen.alphaStr, Gen.alphaUpperStr) {
      (name, extension) ⇒
        whenever(name.length > 0 && extension.length > 0) {
          Given(s"file name and extension - $name, $extension")
          val file = Paths.get(name + "." + extension)

          When("extension is called")
          val retExtension = file.extension

          Then("returned extension must match expected one")
          retExtension.get mustBe extension.toLowerCase
        }
    }
  }

  val subDirs = Table(
    ("path", "expected", "length", "depth"),
    ("/file/data/a7f2b16c188d762fc1b6dca017b4a808cd5b225a4da1c4a3a598d15f98c51866.jpg", "/file/data/a7/f2/a7f2b16c188d762fc1b6dca017b4a808cd5b225a4da1c4a3a598d15f98c51866.jpg", 2, 2),
    ("a7f2b16c188d762fc1b6dca017b4a808cd5b225a4da1c4a3a598d15f98c51866.jpg", "a7/f2/a7f2b16c188d762fc1b6dca017b4a808cd5b225a4da1c4a3a598d15f98c51866.jpg", 2, 2),
    ("a7f2b1.jpg", "a7/f2/a7f2b1.jpg", 2, 2),
    ("/file/data/a7f2b16c188d762fc1b6dca017b4a808cd5b225a4da1c4a3a598d15f98c51866.jpg", "/file/data/a7f2/b16c/188d/a7f2b16c188d762fc1b6dca017b4a808cd5b225a4da1c4a3a598d15f98c51866.jpg", 4, 3),
    ("/file/data/a7f2b16c188d762fc1b6dca017b4a808cd5b225a4da1c4a3a598d15f98c51866.jpg", "/file/data/a7f2b16c188d762fc1b6dca017b4a808cd5b225a4da1c4a3a598d15f98c51866.jpg", 2, 0),
    ("/file/data/a7f2b16c188d762fc1b6dca017b4a808cd5b225a4da1c4a3a598d15f98c51866.jpg", "/file/data/a7f2b16c188d762fc1b6dca017b4a808cd5b225a4da1c4a3a598d15f98c51866.jpg", 0, 2),
    ("/file/data/a7f2b16c188d762fc1b6dca017b4a808cd5b225a4da1c4a3a598d15f98c51866.jpg", "/file/data/a7f2b16c188d762fc1b6dca017b4a808cd5b225a4da1c4a3a598d15f98c51866.jpg", 0, 0)
  )

  "prefixPath" should "add prefixed dirs in front of path" in {
    forAll(subDirs) {
      (path, expected, length, depth) ⇒
        Given(s"path $path, length $length and depth $depth")
        val testPath = Paths.get(path)
        val expectedPath = Paths.get(expected)

        When("extension is called")
        val retPath = testPath.prefixFileName(length, depth)

        Then("returned path must be properly prefixed")
        retPath mustBe expectedPath
    }
  }

  "prefixPath" should "throw exception when negative values passed" in {
    forAll(Gen.alphaStr, Gen.negNum[Int], Gen.negNum[Int]) {
      (path  : String, length: Int, depth: Int) ⇒
        whenever(length < 0 && depth < 0) {
          Given(s"path $path, length $length and depth $depth")
          val testPath = Paths.get(path)

          When("extension is called")
          val thrown = intercept[IllegalArgumentException] {
            testPath.prefixFileName(length, depth)
          }

          thrown must not be null
        }
    }
  }

  "prefixPath" should "throw exception when path too short" in {
    forAll(genAlphaStrN(5)) {
      (path: String) ⇒
        Given(s"path $path")
        val testPath = Paths.get(path)

        When("extension is called")
        val thrown = intercept[IllegalArgumentException] {
          testPath.prefixFileName(2, 3)
        }

        thrown must not be null
    }
  }

  private def genAlphaStrN(size: Int) = {
    Gen.listOfN(size, alphaChar).map(_.mkString)
  }
}
