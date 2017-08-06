package io.clouderite.commons.scala.berries.string

import io.clouderite.commons.scala.berries.string.StringOperations.toStringOperations
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.{FlatSpec, GivenWhenThen, MustMatchers}

class StringOperationsTest extends FlatSpec with MustMatchers with GivenWhenThen with TableDrivenPropertyChecks {

  "sliceLines" must "return proper slice" in {
    Given("test string and expected value")
    val value =
      """
        |Line 1
        |Line 2
        |Line 3
        |Line 4
        |Line 5
      """.stripMargin.trim

    val expected = "Line 2\nLine 3\n"

    val sut = value

    When("sliceLines is executed")
    val retValue = sut.sliceLines(1, 2)

    Then("returned slice must be equal to expected one")
    retValue mustBe expected
  }

  "sliceLines" must "throw exception when from parameter < 0" in {
    val sut = "some string"

    When("sliceLines with from parameter < 0 is executed")
    val thrown = intercept[IllegalArgumentException] {
      sut.sliceLines(-1, 2)
    }

    Then("thrown exception cannot be null and must have proper message")
    thrown must not be null
    thrown must have message "requirement failed: from parameter must be greater than or equal 0"
  }

  "sliceLines" must "throw exception when to parameter < 0" in {
    Given("system under test")
    val sut = "some string"

    When("sliceLines with to parameter < 0 is executed")
    val thrown = intercept[IllegalArgumentException] {
      sut.sliceLines(1, -2)
    }

    Then("thrown exception cannot be null and must have proper message")
    thrown must not be null
    thrown must have message "requirement failed: to parameter must be greater than or equal 0"
  }

  "sliceLines" must "throw exception when from parameter > to parameter" in {
    val sut = "some string"

    When("sliceLines with from parameter > to parameter is executed")
    val thrown = intercept[IllegalArgumentException] {
      sut.sliceLines(2, 1)
    }

    Then("thrown exception cannot be null and must have proper message")
    thrown must not be null
    thrown must have message "requirement failed: from parameter must be lower than or equal to parameter"
  }

  "negSliceLines" must "return proper slice" in {
    Given("test string and expected value")
    val value =
      """
        |Line 1
        |Line 2
        |Line 3
        |Line 4
        |Line 5
      """.stripMargin.trim

    val expected = List("Line 1\n", "Line 4\nLine 5")
    val sut = value

    When("negSliceLines is executed")
    val retValue = sut.negSliceLines(1, 2)

    Then("returned slice must be equal to expected one")
    retValue mustBe expected
  }

  val positiveMatches = Table(
    ("string", "regex"),
    ("scala", "[a-z]*"),
    ("scala", "^[a-z]*$"),
    ("scala", "scala"),
    ("scala", "^scala$")
  )
  
  "matches" must "return true for matching regex" in {
    forAll(positiveMatches) { (value, regex) ⇒
      Given("test string positively matching regex")
      val sut = value

      When("matches is executed")
      val result = sut matches regex

      Then("returned result must be true")
      result mustBe true
    }
  }

  val negativeMatches = Table(
    ("string", "regex"),
    ("scala", "[A-Z]*"),
    ("scala", "scal"),
    ("scala", "^scal"),
    ("scala", "cala$")
  )

  "matches" must "return false for not matching regex" in {
    forAll(negativeMatches) { (value, regex) ⇒
      Given("test string negatively matching regex")
      val sut = value

      When("matches is executed")
      val result = sut matches regex

      Then("returned result must be false")
      result mustBe false
    }
  }
}
