package io.clouderite.commons.scala.berries.time

import java.time.Duration
import java.time.temporal.{ChronoUnit, TemporalAmount}
import java.util.concurrent.TimeUnit.NANOSECONDS

import scala.concurrent.duration.FiniteDuration

class DurationOperations(finiteDuration: FiniteDuration)

class JavaDurationOperations(duration: Duration) {
  def toScala: FiniteDuration =
    FiniteDuration(duration.toNanos, NANOSECONDS)
}

object DurationOperations {
  implicit def toDurationOperations(finiteDuration: FiniteDuration): DurationOperations =
    new DurationOperations(finiteDuration)

  implicit def toJavaDurationOperations(duration: Duration): JavaDurationOperations =
    new JavaDurationOperations(duration)

  implicit def toTemporalAmount(finiteDuration: FiniteDuration): TemporalAmount = {
    val value = finiteDuration.toMillis
    Duration.of(value, ChronoUnit.MILLIS)
  }
}
