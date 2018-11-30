import models._

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._
import play.api.data.validation._

import org.scalatest._
import org.scalatestplus.play._


class ReadsSpec extends FlatSpec with Matchers {

    "User reads" should "parse a valid json" in {
        val userJson = Json.obj("username" -> "andrew", "password" -> "123456")
        val user = userJson.as[User]
        assert(user.id == 0)
        assert(user.username == "andrew")
        assert(user.password == "123456")
        assert(user.is_deleted == false)
    }

    "User reads" should "parse a valid json with extra fields" in {
        val userJson = Json.obj("username" -> "andrew", "age" -> 20, "password" -> "123456")
        val user = userJson.as[User]
        assert(user.id == 0)
        assert(user.username == "andrew")
        assert(user.password == "123456")
        assert(user.is_deleted == false)
    }

    "User reads" should "not parse a json with password length < 6" in {
        val userJson = Json.obj( "username" -> "andrew", "password" -> "123")
        assertThrows[JsResultException] {
            userJson.as[User]
        }
    }

    "User reads" should "not parse a json with password length > 8" in {
        val userJson = Json.obj("username" -> "andrew", "password" -> "123456789")
        assertThrows[JsResultException] {
            userJson.as[User]
        }
    }

    "User reads" should "not parse a json without a password" in {
        val userJson = Json.obj("username" -> "andrew")
        assertThrows[JsResultException] {
            userJson.as[User]
        }
    }

    "User reads" should "not parse a json without a username" in {
        val userJson = Json.obj("password" -> "123123")
        assertThrows[JsResultException] {
            userJson.as[User]
        }
    }
}