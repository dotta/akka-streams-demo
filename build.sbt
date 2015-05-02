name := """akka-streams-demo"""

version := "0.1"

lazy val commonSettings = Seq(
  scalaVersion := "2.11.6",
  libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-stream-experimental" % "1.0-RC2"
  )
)

lazy val seed = project
  .in(file(".") / "seed-demo")
  .settings(commonSettings: _*)

lazy val demo = project
  .in(file(".") / "demo")
  .settings(commonSettings: _*)
