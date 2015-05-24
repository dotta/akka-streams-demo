import com.typesafe.sbteclipse.core._

name := """akka-streams-demo"""

version := "0.1"

initialize := {
  val _ = initialize.value
  if (sys.props("java.specification.version") != "1.8")
    sys.error("Java 8 is required for this project.")
}

lazy val commonSettings = Seq(
  scalaVersion := "2.11.6",
  libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-stream-experimental" % "1.0-RC3"
  ),
  javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint")
)

lazy val javaSettings = Seq(
  // EclipseKeys.projectFlavor := EclipseProjectFlavor.Java // this requires my own patched sbteclipse (I'm working on a PR...)
)

lazy val scalaSeed = project
  .in(file(".") / "scala-demo-seed")
  .settings(commonSettings: _*)

lazy val scalaDemo = project
  .in(file(".") / "scala-demo")
  .settings(commonSettings: _*)

lazy val javaDemo = project
  .in(file(".") / "java-demo")
  .settings(commonSettings: _*)
  .settings(javaSettings: _*)
