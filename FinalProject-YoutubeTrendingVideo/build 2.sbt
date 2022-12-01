name := "FinalProject-YoutubeTrendingVideo"
version := "0.1"
scalaVersion := "2.12.12"
val sparkVersion = "3.2.1"

libraryDependencies ++= Seq(  "com.phasmidsoftware" %% "tableparser" % "1.0.14",  "com.github.nscala-time" %% "nscala-time" % "2.24.0",  "org.scalatest" %% "scalatest" % "3.2.3" % "test",  "org.apache.spark" %% "spark-core" % sparkVersion,  "org.apache.spark" %% "spark-sql" % sparkVersion,  "org.apache.spark" %% "spark-mllib" % sparkVersion,  "org.scalactic" %% "scalactic" % "3.0.1",  "org.scalatest" %% "scalatest" % "3.0.1" % "test")
