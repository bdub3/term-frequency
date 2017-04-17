name := "TermFrequency"

version := "1.0"

scalaVersion := "2.12.1"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "test"

scalacOptions ++= Seq("-feature")

mainClass in (Compile, run) := Some("TermFrequency")
        