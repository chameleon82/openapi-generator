version := ""
name := "scala-resteasy-akka-http-petstore-server"
organization := ""
scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
  "javax.ws.rs" % "javax.ws.rs-api" % "2.0.1",
  "io.swagger.core.v3" % "swagger-core" % "2.0.8",
  "io.swagger.core.v3" % "swagger-annotations" % "2.0.8",
  "io.swagger.core.v3" % "swagger-models" % "2.0.8",
  "io.swagger.core.v3" % "swagger-jaxrs2" % "2.0.8",
  "com.typesafe.akka" %% "akka-http" % "10.1.8",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.8",
  "com.typesafe.akka" %% "akka-actor" % "2.5.23",
  "com.typesafe.akka" %% "akka-stream" % "2.5.23",
  "org.scalatest" %% "scalatest" % "3.0.4" % "test"
)

resolvers ++= Seq(
  Resolver.mavenLocal
)

scalacOptions := Seq(
  "-unchecked",
  "-deprecation",
  "-feature"
)

publishArtifact in (Compile, packageDoc) := false

