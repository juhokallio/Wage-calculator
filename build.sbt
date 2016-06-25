name := "wageCalculater"

version := "0.1"

scalaVersion := "2.11.8"

lazy val root = (project in file("."))
  .settings(libraryDependencies ++= Seq(
    "org.scalactic" %% "scalactic" % "2.2.6",
    "org.scalatest" %% "scalatest" % "2.2.6" % "test"
  ))
  // Copy all managed dependencies to \lib_managed directory
  .settings(retrieveManaged := true)
