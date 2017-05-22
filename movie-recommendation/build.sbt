name := "movie-recommendation"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= {
  val akkaV       = "2.5.1"
  val akkaHttpV   = "10.0.6"
  val scalaTestV  = "3.0.3"

  Seq(
    "org.apache.spark" %% "spark-core" % "2.1.1",
    "org.apache.spark" %% "spark-mllib" % "2.1.1",
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "com.typesafe.akka" %% "akka-stream" % akkaV,
    "com.typesafe.akka" %% "akka-testkit" % akkaV,
    "com.typesafe.akka" %% "akka-http" % akkaHttpV,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpV,
    "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpV,
    "org.scalatest"     %% "scalatest" % scalaTestV % "test",
    "io.monix" %% "shade" % "1.9.5"
  )
}