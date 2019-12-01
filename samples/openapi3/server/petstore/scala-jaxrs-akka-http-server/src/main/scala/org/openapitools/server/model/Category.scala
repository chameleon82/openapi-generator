/**
* OpenAPI Petstore
* This is a sample server Petstore server. For this sample, you can use the api key `special-key` to test the authorization filters.
*
* The version of the OpenAPI document: 1.0.0
* 
*
* NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
* https://openapi-generator.tech
* Do not edit the class manually.
*/

package org.openapitools.server.model

import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints._;
import javax.validation.Valid;

@Schema(name = "Category", title = "Pet category", description = "A category for a pet")
case class Category (
  
  @Schema(name = "id", `type` = "Long")
  id: Option[Long] = None,
   @Pattern(regexp="/^[a-zA-Z0-9]+[a-zA-Z0-9\\.\\-_]*[a-zA-Z0-9]+$/")
  @Schema(name = "name", `type` = "String")
  name: Option[String] = None
)

