ThisBuild / version := "0.1.0-SNAPSHOT"

scalaVersion := "2.12.17"

name := "Storage"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "3.2.1",
  "org.apache.spark" %% "spark-sql" % "3.2.1",
  "org.apache.spark" %% "spark-streaming" % "3.2.1",
  "org.apache.spark" %% "spark-streaming-kafka-0-10" % "3.2.1",
  "com.typesafe.play" %% "play-json" % "2.8.0",
  "javax.mail" % "javax.mail-api" % "1.6.2",
  "com.sun.mail" % "javax.mail" % "1.6.2",
  "org.scala-lang" % "scala-library" % scalaVersion.value
)
