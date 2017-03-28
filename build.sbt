organization := "io.scalaberries"
name := "berries"
version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= {
  val akkaV = "2.4.16"
  val akkaHttpV = "10.0.4"
  val scalazVersion = "7.1.9"
  Seq(
    "org.scala-lang" % "scala-reflect" % scalaVersion.value,
    "com.typesafe.akka"   %% "akka-actor" % akkaV,
    "com.typesafe.akka"   % "akka-http-spray-json-experimental_2.11" % "2.4.11",

    "org.scalacheck" % "scalacheck_2.11" % "1.13.5" % "test",
    "org.scalatest" %% "scalatest" % "3.0.0" % "test",
    "org.mockito" % "mockito-core" % "1.10.19" % "test"
  )
}

fork := true
scalacOptions ++= Seq("-Xmax-classfile-name", "110")