val scalatest = "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"

//scalacOptions in Test ++= Seq("-Yrangepos")

lazy val commonSettings = Seq(
  organization := "uk.co.exware",
  version := "0.1.0",
  scalaVersion := "2.11.5"
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "exchange-system2",
    libraryDependencies ++= Seq(scalatest)
  )
