name := "CsvParser"

mainClass in assembly := some("org.hshabbir.practice.csv_parser.Main")
assemblyJarName := "my_cookie.jar"
version := "0.1"
scalaVersion := "2.13.4"

libraryDependencies ++= Seq(
  "info.picocli" % "picocli" % "4.5.2",
  "org.scala-lang.modules" %% "scala-parallel-collections" % "1.0.0",
  "joda-time" % "joda-time" % "2.10.8",
  "org.scalatest" %% "scalatest" % "3.2.2" % "test"
)

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}