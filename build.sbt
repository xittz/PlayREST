name := """play-scala-slick-example"""

version := "2.6.x"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.6"

crossScalaVersions := Seq("2.11.12", "2.12.4")

libraryDependencies += guice
libraryDependencies += "com.typesafe.play" %% "play-slick" % "3.0.3"
libraryDependencies += "com.typesafe.play" %% "play-slick-evolutions" % "3.0.3"

libraryDependencies += "com.h2database" % "h2" % "1.4.197"

libraryDependencies ++= Seq(
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.0.0" % "test"
)

libraryDependencies ++= Seq(
  "org.mockito" % "mockito-core" % "2.21.0" % Test
)