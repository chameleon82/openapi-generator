package org.openapitools.server

import java.lang.annotation.Annotation

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.{Contact, Info, License}
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.extensions.Extension
import io.swagger.v3.oas.annotations.servers.Servers
import io.swagger.v3.oas.models.servers.Server
import io.swagger.v3.oas.models.{ExternalDocumentation, OpenAPI}


@OpenAPIDefinition(
  info = new Info(
    title = "Some title",
    description = "Some description",
    termsOfService = "Terms here",
    contact = new Contact(name = "Name of cont", url = "URL", email = "e@ma.il"),
    license = new License(name = "lice", url = "http://nce", extensions = Array())
  ),
  tags = Array(
    new Tag(name="TAGG", description = "TEEGG")
  )
)
trait SwaggerDocService {


}
