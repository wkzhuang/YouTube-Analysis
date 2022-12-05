name := "YouTube Access"
version := "1.0"
scalaVersion := "2.12.15"


libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.14" % "test",
  "org.apache.spark" %% "spark-sql" % "3.3.1",
  "com.google.apis" % "google-api-services-youtube" % "v3-rev20220926-2.0.0",
  "com.google.api-client" % "google-api-client-gson" % "2.0.1", // for GsonFactory
  //"com.google.http-client" % "google-http-client-gson" % "1.42.3"  // for Jackson2 ??
  "org.scalatest" %% "scalatest" % "3.2.14" % "test"

)