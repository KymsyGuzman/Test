ThisBuild / version := "0.1.0-SNAPSHOT"

scalaVersion := "2.12.17"
name := "alert"
//fork := true
javaOptions += "--add-exports java.base/sun.nio.ch=ALL-UNNAMED"
libraryDependencies ++= Seq(
  "log4j" % "log4j" % "1.2.17",
  "com.typesafe.play" %% "play-json" % "2.9.2",
  //"org.apache.kafka" % "kafka-clients" % "2.8.0",
  "org.apache.kafka" % "kafka-clients" % "2.8.1", 
  "ch.qos.logback" % "logback-classic" % "1.2.3", 
  "org.slf4j" % "slf4j-api" % "1.7.30" ,
  //"org.apache.spark" %% "spark-streaming-kafka-0-10" % "3.4.0",
  //"org.apache.spark" %% "spark-streaming" % "3.4.0",
  //"org.apache.spark" %% "spark-core" % "3.4.0",
  //"org.apache.spark" %% "spark-streaming-kafka-0-10" % "3.2.1",
  //"org.apache.spark" %% "spark-streaming" % "3.2.1",
  //"org.apache.spark" %% "spark-core" % "3.2.1",
  //"org.apache.spark" %% "spark-core" % "2.4.0",
  //"org.apache.spark" %% "spark-streaming" % "2.4.0" ,
  //"org.apache.spark" %% "spark-streaming-kafka-0-10" % "2.4.0",
  //"org.apache.kafka" % "kafka-clients" % "2.8.0",
  //"com.typesafe.play" %% "play-json" % "2.8.0",
  //"com.fasterxml.jackson.core" % "jackson-databind" % "2.6.7",
  "javax.mail" % "javax.mail-api" % "1.6.2",
  "com.sun.mail" % "javax.mail" % "1.6.2",
  "org.scala-lang" % "scala-library" % scalaVersion.value,
  
)
//javaHome := Some(file("/usr/bin/java"))
//dependencyOverrides += "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.12.3"
//dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-core" % "2.12.3"
//dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-databind" %  "2.12.3"
//dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-core" % "2.6.7"
//dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-databind" % "2.6.7"
//dependencyOverrides += "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.6.7"

