name := """YoutubeTrendingVideos"""
organization := "team2"

version := "1.0-SNAPSHOT"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(
    name := """play-scala-forms-example""",
    version := "2.8.x",
    scalaVersion := "2.13.10",
    libraryDependencies ++= Seq(
      guice,
      "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test,
    ),
    scalacOptions ++= Seq(
      "-feature",
      "-deprecation",
      "-Xfatal-warnings"
    )
  )

val sparkVersion = "3.2.1"

libraryDependencies += guice
libraryDependencies ++= Seq(  "com.phasmidsoftware" %% "tableparser" % "1.0.14",  "com.github.nscala-time" %% "nscala-time" % "2.24.0",  "org.scalatest" %% "scalatest" % "3.2.3" % "test",  "org.apache.spark" %% "spark-core" % sparkVersion,  "org.apache.spark" %% "spark-sql" % sparkVersion,  "org.apache.spark" %% "spark-mllib" % sparkVersion,  "org.scalactic" %% "scalactic" % "3.2.14",  "org.scalatest" %% "scalatest" % "3.2.14" % "test")
// Adds additional packages into Twirl
//TwirlKeys.templateImports += "team2.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "team2.binders._"
