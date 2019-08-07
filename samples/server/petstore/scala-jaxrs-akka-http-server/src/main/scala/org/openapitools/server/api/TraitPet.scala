package org.openapitools.server.api

import scala.concurrent.Future

trait TraitPet {
  def mew(int: Int): Future[String] = ???
}

object TraitPet extends TraitPet
