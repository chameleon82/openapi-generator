package org.openapitools.server

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives._
import io.swagger.v3.core.util.Yaml
import io.swagger.v3.jaxrs2.Reader
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.integration.api.{OpenAPIConfigBuilder, OpenAPIConfiguration}
import io.swagger.v3.oas.models.OpenAPI
import org.openapitools.server.api.{OpenApiSpec, PetApiSpec, StoreApiSpec, UserApiSpec}

import scala.collection.JavaConverters._

object Boot {
  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()
  // needed for the future flatMap/onComplete in the end
  implicit val executionContext = system.dispatcher

  def main(args: Array[String]) {
    val route =
      path("swagger.yaml") {
        get {
          complete(
            Yaml.pretty().writeValueAsString {
              val classes: Set[Class[_]] = Set(
                classOf[OpenApiSpec],
                classOf[PetApiSpec],
                classOf[StoreApiSpec],
                classOf[UserApiSpec]
              )
              //val op = new OpenAPI()
              //new OpenAPIConfiguration()
              new Reader().read(classes.asJava)
            }
          )
        }
      }
    Http().bindAndHandle(route, "0.0.0.0", 8080)
  }
}
