version := ""
name := "scala-resteasy-petstore-spec"
organization := ""
scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
    "javax.ws.rs" % "javax.ws.rs-api" % "2.0.1",
    "io.swagger.core.v3" % "swagger-core" % "2.0.8",
    "io.swagger.core.v3" % "swagger-annotations" % "2.0.8",
    "io.swagger.core.v3" % "swagger-models" % "2.0.8",
    "io.swagger.core.v3" % "swagger-jaxrs2" % "2.0.8",
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

