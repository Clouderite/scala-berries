val akkaV = "2.5.6"
val akkaHttpV = "10.0.10"

lazy val commonSettings = Seq(
  scalaVersion := "2.12.1",
  crossScalaVersions := Seq("2.11.8", "2.12.1"),

  organization := "io.clouderite.commons",
  name := "scala-berries",

  libraryDependencies ++= {
    Seq(
      "org.scala-lang" % "scala-reflect" % scalaVersion.value,
      "com.typesafe.akka" %% "akka-actor" % akkaV,
      "com.typesafe.akka" %% "akka-cluster" % akkaV,
      "com.typesafe.akka" %% "akka-cluster-tools" % akkaV,
      "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpV,
      "com.typesafe.akka" %% "akka-testkit" % akkaV,
      "org.scalaz" %% "scalaz-core" % "7.2.14",

      "org.scalacheck" %% "scalacheck" % "1.13.5" % "test",
      "org.scalatest" %% "scalatest" % "3.0.0" % "test",
      "org.mockito" % "mockito-core" % "1.10.19" % "test"
    )
  },

  fork := true,
  scalacOptions ++= Seq("-Xmax-classfile-name", "110")
)

lazy val publishSettings = Seq(
  ivyLoggingLevel := UpdateLogging.Full,
  publishArtifact := true,
  publishArtifact in Test := false,
  publishMavenStyle := true,
  pomIncludeRepository := { _ => false },

  credentials += Credentials(Path.userHome / ".ivy2" / ".credentials"),

  publishTo := Some(
    if (isSnapshot.value)
      "Clouderite Snapshots Nexus" at "https://maven.clouderite.io/nexus/content/repositories/snapshots/"
    else
      "Clouderite Releases Nexus" at "https://maven.clouderite.io/nexus/content/repositories/releases/"
  )
)

val slackNotify = ReleaseStep(action = st => {
  import net.gpedro.integrations.slack.{SlackApi, SlackMessage}
  import scala.io.Source
  val api = new SlackApi(Source.fromFile("slack.webhook").mkString)

  val extracted = Project.extract(st)
  val version = extracted.get(Keys.version)
  val group = extracted.get(Keys.organization)
  val artifact = extracted.get(Keys.name)
  api.call(new SlackMessage(s"""New release "$group" %% "$artifact" % "$version"""))
  st
})

import sbtrelease.ReleasePlugin.autoImport.ReleaseTransformations._
releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  slackNotify,
  commitReleaseVersion,
  tagRelease,
  publishArtifacts,
  setNextVersion,
  commitNextVersion,
  pushChanges
)

lazy val app = project
  .in(file("."))
  .settings(commonSettings)
  .settings(publishSettings)

