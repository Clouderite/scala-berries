package io.clouderite.commons.scala.berries.akka

import akka.actor.{ActorContext, ActorRef}
import akka.cluster.routing.{ClusterRouterGroup, ClusterRouterGroupSettings}
import akka.routing.ConsistentHashingRouter.ConsistentHashMapping
import akka.routing.{ConsistentHashingGroup, Group, RandomGroup, TailChoppingGroup}

import scala.concurrent.duration.FiniteDuration

case class ClusterRouterGroupSpecification(name: String, path: String, role: String) {
  def routerSettings(implicit context: ActorContext): ClusterRouterGroupSettings = {
    ClusterRouterGroupSettings(totalInstances = 100, routeesPaths = List(path), allowLocalRoutees = true, useRole = Some(role))
  }
}

case class TailChoppingSpecification(within: FiniteDuration, interval: FiniteDuration) {
  def routerStrategy(implicit context: ActorContext): Group = {
    TailChoppingGroup(Nil, within = within, interval = interval)
  }
}

object ClusterRouterGroupFactory {
  def consistentHashingGroup(specification: ClusterRouterGroupSpecification)(hashMapping: ConsistentHashMapping)(implicit context: ActorContext): ActorRef = {
    context.actorOf(
      ClusterRouterGroup(
        ConsistentHashingGroup(Nil, hashMapping = hashMapping),
        specification.routerSettings
      ).props(), name = specification.name)
  }

  def randomGroup(specification: ClusterRouterGroupSpecification)(implicit context: ActorContext): ActorRef = {
    context.actorOf(
      ClusterRouterGroup(
        RandomGroup(Nil),
        specification.routerSettings
      ).props(), name = specification.name)
  }

  def tailChoppingGroup(specification: ClusterRouterGroupSpecification, choppingSpecification: TailChoppingSpecification)(implicit context: ActorContext): ActorRef = {
    context.actorOf(ClusterRouterGroup(
      choppingSpecification.routerStrategy,
      specification.routerSettings)
      .props(), name = specification.name)
  }
}
