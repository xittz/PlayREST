package controllers

import javax.inject._

import models._
import daos._
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.i18n._
import play.api.libs.json._
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

class UserJsonController @Inject()(repo: UserRepository,
                                  cc: MessagesControllerComponents
                                )(implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {

  def addUser = Action.async(BodyParsers.parse.json) { implicit request =>
    request.body.validate[User].fold(
      error => {
        Future.successful(BadRequest("Couldn't parse JSON"))
      },
      user => {
        repo.create(user.username, user.password).map { _ => 
          Created("User " + user.username + " is created")
        }
      }
    )
  }

  def updateUser(id: Long) = Action.async(parse.json) { implicit request =>
    request.body.validate[User].fold(
      error => {
        Future.successful(BadRequest("Couldn't parse JSON"))
      },
      user => {
        repo.exists(id).map { exists => 
          if (exists) {
            repo.update(id, user.username, user.password)
              Ok("updated user with id = " + id)
          } else {
            NotFound("user with id = " + id + " is not found")
          }
        }
      }
    )
  }

  def deleteUser(id: Long) = Action.async { implicit request =>
    repo.exists(id).map { exists => 
      if (exists) {
        repo.delete(id)
        Ok("user with id = " + id + " is deleted")
      } else {
        NotFound("user with id = " + id + " is not found")
      }
    }
  }

  def getUsers = Action.async { implicit request =>
    repo.list().map { users =>
      Ok(Json.toJson(users))
    }
  }

  def getUser(id: Long) = Action.async { implicit request =>
    repo.find(id).map { users =>
      Ok(Json.toJson(users))
    }
  }

  def getUsernames = Action.async { implicit request => 
    repo.usernames().map { usernames => 
      Ok(Json.toJson(usernames))
    }
  }
}