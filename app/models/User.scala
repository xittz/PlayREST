package models

import play.api.libs.json._
import play.api.libs.functional.syntax._

case class User(username: String, password: String)

object User {  
  implicit val userJsonFormat = Json.format[User]
  
  implicit val userReads: Reads[User] = (
    (JsPath \ "username").read[String] and
    (JsPath \ "password").read[String]
  )(User.apply _)
}

