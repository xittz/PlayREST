package models

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._
import play.api.data.validation._

case class User(id: Long = 0, username: String, password: String, isDeleted: Boolean = false)

object User {  

  implicit val userReads: Reads[User] = (
    Reads.pure(0: Long) and
    (JsPath \ "username").read[String] and
    (JsPath \ "password").read[String](minLength[String](6)) and
    Reads.pure(false: Boolean)
  )(User.apply _)

  implicit val userWrites: Writes[User] = (
    (JsPath \ "id").write[Long] and
    (JsPath \ "username").write[String] and
    (JsPath \ "password").write[String] and
    (JsPath \ "isDeleted").write[Boolean]
  )(unlift(User.unapply))
}

