package io.clouderite.commons.scala.berries.akka

import akka.actor.{ActorRef, ActorRefFactory, Props}
import akka.pattern.{Backoff, BackoffSupervisor}
import akka.routing.{DefaultResizer, RandomPool}

import scala.concurrent.duration.{DurationLong, FiniteDuration}
import scala.reflect.runtime.universe._

trait ActorFactory[T] {
  val actorName: String
  val dispatcher = "akka.actor.default-dispatcher"
  val mailbox = "akka.actor.default-mailbox"
  val minBackoff: FiniteDuration = 1.seconds
  val maxBackoff: FiniteDuration = 10.seconds
  val randomFactorBackoff = 0.2
  val dependencies: Array[Any] = Array.empty
  val poolSize: Int = 1
  val minPoolSize: Int = poolSize
  val maxPoolSize: Int = poolSize

  def actor(implicit actorFactory: ActorRefFactory, tag: TypeTag[T]): ActorRef = {
    actor(dependencies: _*)
  }

  def actor(dependencies: Any*)(implicit actorFactory: ActorRefFactory, tag: TypeTag[T]): ActorRef = {
    createActor(props(dependencies: _*))
  }

  def actorWithBackoff(dependencies: Any*)(implicit actorFactory: ActorRefFactory, tag: TypeTag[T]): ActorRef = {
    createActor(backoffProps(dependencies: _*))
  }

  private def createActor(props: Props)(implicit actorFactory: ActorRefFactory) = {
    val actorRef = if (poolSize == 1) {
      actorFactory.actorOf(props, actorName)
    } else {
      val poolResizer = DefaultResizer(lowerBound = minPoolSize, upperBound = maxPoolSize)
      actorFactory.actorOf(RandomPool(poolSize, resizer = Some(poolResizer)).props(props), actorName)
    }

    postCreate(actorRef)
    actorRef
  }

  private def backoffProps(dependencies: Any*)(implicit tag: TypeTag[T]): Props = {
    BackoffSupervisor.props(
      Backoff.onFailure(
        props(dependencies: _*),
        "back",
        minBackoff,
        maxBackoff,
        randomFactorBackoff
      )
    )
  }

  private def props(dependencies: Any*)(implicit tag: TypeTag[T]): Props = {
    baseProps(dependencies: _*).withDispatcher(dispatcher).withMailbox(mailbox)
  }

  def baseProps(dependencies: Any*)(implicit tag: TypeTag[T]): Props = {
    Props(tag.mirror.runtimeClass(tag.tpe.typeSymbol.asClass), dependencies: _*)
  }

  def postCreate(actorRef: ActorRef): Unit = {
  }
}
