package org.openapitools.server.marshal

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, JsObject, JsString, JsValue, RootJsonFormat}

import scala.reflect.ClassTag

trait DefaultJsonFormats extends DefaultJsonProtocol with SprayJsonSupport {

  def jsonObjectFormat[A: ClassTag]: RootJsonFormat[A] = new RootJsonFormat[A] {
    private val ct: ClassTag[A] = implicitly[ClassTag[A]]
    override def read(json: JsValue): A = ct.runtimeClass.newInstance().asInstanceOf[A]

    override def write(obj: A): JsValue = JsObject("value" -> JsString(ct.runtimeClass.getSimpleName))
  }
}
