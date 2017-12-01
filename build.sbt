name := "Akka-Http Example"

version := "1.0"

scalaVersion := "2.11.12"

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-library" % "2.11.12",
  "com.typesafe" % "config" % "1.3.2",
  "com.typesafe.akka" %% "akka-http" % "10.0.10",
  "com.typesafe.akka" %% "akka-stream" % "2.5.4",
  "com.typesafe.akka" %% "akka-actor" % "2.5.4",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.10",
  "com.github.etaty" %% "rediscala" % "1.8.0",
  "org.specs2" %% "specs2-core" % "4.0.0" % "test"
)

mainClass in reStart := Some("com.example.App")

scalacOptions in Test ++= Seq("-Yrangepos")
