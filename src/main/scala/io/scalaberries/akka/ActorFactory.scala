package io.scalaberries.akka

import akka.actor.{ActorRef, ActorRefFactory, Props}
import akka.pattern.{Backoff, BackoffSupervisor}

import scala.concurrent.duration.{DurationLong, FiniteDuration}

trait ActorFactory {
  val actorClass: Class[_]
  val actorName: String
  val dispatcher = "akka.main-dispatcher"
  val mailbox = "akka.main-mailbox"
  val minBackoff: FiniteDuration = 1.seconds
  val maxBackoff: FiniteDuration = 10.seconds
  val randomFactorBackoff = 0.2

  def actor(implicit actorFactory: ActorRefFactory): ActorRef = {
    actorFactory.actorOf(backoffProps)
  }

  private def backoffProps: Props = {
    BackoffSupervisor.props(Backoff.onStop(
      props,
      actorName,
      minBackoff,
      maxBackoff,
      randomFactorBackoff
    ))
  }

  private def props: Props = {
    baseProps.withDispatcher(dispatcher).withMailbox(mailbox)
  }

  def baseProps: Props = {
    Props(actorClass)
  }
}
