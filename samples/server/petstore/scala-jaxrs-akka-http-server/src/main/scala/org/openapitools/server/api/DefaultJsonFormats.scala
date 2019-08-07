package org.openapitools.server.api

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, DeserializationException, JsObject, JsString, JsValue, RootJsonFormat}

import scala.reflect.ClassTag
import java.time.LocalDate
import java.time.format.DateTimeFormatter

trait DefaultJsonFormats extends DefaultJsonProtocol with SprayJsonSupport {

  implicit object DateJsonFormat extends RootJsonFormat[LocalDate] {
     override def write(obj: LocalDate) = JsString(obj.format(DateTimeFormatter.ISO_DATE))

     override def read(json: JsValue): LocalDate = json match {
       case JsString(s) => LocalDate.parse(s)
       case _ => throw DeserializationException("Date Field must be in ISO-8601 format")
     }
  }

  def jsonObjectFormat[A: ClassTag]: RootJsonFormat[A] = new RootJsonFormat[A] {
    private val ct: ClassTag[A] = implicitly[ClassTag[A]]
    override def read(json: JsValue): A = ct.runtimeClass.newInstance().asInstanceOf[A]
    override def write(obj: A): JsValue = JsObject("value" -> JsString(ct.runtimeClass.getSimpleName))
  }
}
