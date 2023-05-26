ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.2.1"

lazy val Simulator = project
lazy val Storage = project
lazy val alert = project
lazy val Analyse = project

lazy val root = (project in file("."))
  .settings(
    name := "sbt-project"
  )
  .aggregate(Simulator, Storage, alert, Analyse)