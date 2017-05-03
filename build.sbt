import java.util.{Date, TimeZone}

def timestamp(): String = {
  val sdf = new java.text.SimpleDateFormat("yyyyMMddHHmmss")
  sdf.setTimeZone(TimeZone.getTimeZone("UTC"))
  sdf.format(new Date(System.currentTimeMillis()))
}

lazy val commonSettings = Seq(
  scalaVersion := "2.11.8",

  organization := "io.clouderite.commons",
  name := "scala-berries",
  version := "1.0.1." + timestamp(),

  ivyLoggingLevel := UpdateLogging.Full,
  publishArtifact := true,
  publishArtifact in Test := false,
  publishMavenStyle := true,
  pomIncludeRepository := { _ => false },
  publishTo := Some("Sonatype Releases Nexus" at "https://maven.clouderite.io/nexus/content/repositories/releases/"),
  credentials += Credentials(Path.userHome / ".ivy2" / ".credentials"),

  resolvers += "Sonatype Releases Nexus" at "https://maven.clouderite.io/nexus/content/repositories/releases/",

  libraryDependencies ++= {
    val akkaV = "2.5.0"
    Seq(
      "org.scala-lang" % "scala-reflect" % scalaVersion.value,
      "com.typesafe.akka" %% "akka-actor" % akkaV,
      "com.typesafe.akka" %% "akka-cluster" % akkaV,
      "com.typesafe.akka" %% "akka-cluster-tools" % akkaV,
      "com.typesafe.akka" % "akka-http-spray-json-experimental_2.11" % "2.4.11",

      "org.scalacheck" % "scalacheck_2.11" % "1.13.5" % "test",
      "org.scalatest" %% "scalatest" % "3.0.0" % "test",
      "org.mockito" % "mockito-core" % "1.10.19" % "test"
    )
  },

  fork := true,
  scalacOptions ++= Seq("-Xmax-classfile-name", "110")
)

lazy val app = project
  .in(file("."))
  .settings(commonSettings)

