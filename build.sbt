import AssemblyKeys._

assemblySettings

// first two lines are for sbt-assembly

name := "Lightsaber"

version := "1.0"

scalaVersion := "2.10.3"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq("com.typesafe.akka" %% "akka-actor" % "2.3.2")

scalacOptions += "-deprecation"


