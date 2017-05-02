package io.clouderite.commons.scala.berries.time

import java.time.Duration
import java.time.temporal.{ChronoUnit, TemporalAmount}

import scala.concurrent.duration.FiniteDuration

class DurationOperations(finiteDuration: FiniteDuration) {
}

object DurationOperations {
  implicit def toDurationOperations(finiteDuration: FiniteDuration): DurationOperations =
    new DurationOperations(finiteDuration)

  implicit def toTemporalAmount(finiteDuration: FiniteDuration): TemporalAmount = {
    val value = finiteDuration.toMillis
    Duration.of(value, ChronoUnit.MILLIS)
  }
}
