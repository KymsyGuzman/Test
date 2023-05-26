ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.17"

lazy val root = (project in file("."))
  .settings(
    name := "Simulator",
    libraryDependencies ++= Seq(
      "com.typesafe.play" %% "play-json" % "2.9.2",
      "org.apache.kafka" % "kafka-clients" % "2.8.0",
      "log4j" % "log4j" % "1.2.17"
    )
  )
