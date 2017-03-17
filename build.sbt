organization := "io.scalaberries"
name := "berries"
version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= {
  val akkaV = "2.4.16"
  val akkaHttpV = "10.0.4"
  val scalazVersion = "7.1.9"
  Seq(
    "com.typesafe.akka"   %% "akka-actor" % akkaV,
    "org.scalatest"       %% "scalatest" % "2.2.6" % "test",
    "org.mockito"         % "mockito-core" % "1.10.19" % "test"
  )
}

fork := true
scalacOptions ++= Seq("-Xmax-classfile-name", "110")