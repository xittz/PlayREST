package models

import play.api.libs.json._
import play.api.libs.functional.syntax._

case class UserInfo(id: Long, username: String, password: String)
object UserInfo {
  implicit val userInfoFormat = Json.format[UserInfo]
}
