package controllers

import javax.inject._

import services._
import models._
import daos._
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.i18n._
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

class UserController @Inject()(service: UserService,
                                  cc: MessagesControllerComponents
                                )(implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {


  def addUser = Action.async(parse.json[User]) { implicit request =>
    service.create(request.body).map { _ =>
      Created("User " + request.body.username + " is created")
    }
  }

  def updateUser(id: Long) = Action.async(parse.json[User]) { implicit request =>
    service.find(id).map {
      case None => NotFound("user with id = " + id + " is not found")
      case Some(user) => {
        service.update(id, request.body)
        Ok("updated user with id = " + id)
      }
    }
  }

  def deleteUser(id: Long) = Action.async { implicit request =>
    service.find(id).map {
      case None => NotFound("user with id = " + id + " is not found")
      case Some(_) => {
        service.delete(id)
        Ok("user with id = " + id + " is deleted")
      }
    }
  }

  def getUsers = Action.async { implicit request =>
    service.list().map { users =>
      Ok(Json.toJson(users))
    }
  }

  def getUser(id: Long) = Action.async { implicit request =>
    service.find(id).map { users =>
      Ok(Json.toJson(users))
    }
  }

  def getUsernames = Action.async { implicit request => 
    service.usernames().map { usernames => 
      Ok(Json.toJson(usernames))
    }
  }
}