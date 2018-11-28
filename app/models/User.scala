package models

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._
import play.api.data.validation._

case class User(id: Long = 0, username: String, password: String, isDeleted: Boolean = false)

object User {  

  implicit val userReads: Reads[User] = (
    Reads.pure(0: Long) and
    (__ \ "username").read[String] and
    (__ \ "password").read[String](minLength[String](6)) and
    Reads.pure(false: Boolean)
  )(User.apply _)

  implicit val userWrites: Writes[User] = (u: User) => Json.obj(
      "id" -> u.id,
      "username" -> u.username,
      "password" -> u.password,
      "isDeleted" -> u.isDeleted
  )
}

