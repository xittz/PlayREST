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

class UserController @Inject()(
  service: UserService,
  authAction: UserAuthAction,
  cc: MessagesControllerComponents)(implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {

  /*def authRequired[A](action: Action[A]) = Action.async(action.parser) { request => 
    request.headers.get("authToken").collect {
      case "123123" => action(request)
    } getOrElse {
      Future.successful(Forbidden("auth token is required"))
    }
  }*/

  def addUser = authAction.async { implicit request => 
    service.create(request.body).map { _ => 
      Created("user " + request.body.username + " is created")
    }
  }

  def updateUser(id: Long) = authAction.async { implicit request =>
    service.find(id).map {
      case None => NotFound("user with id = " + id + " is not found")
      case Some(user) => {
        service.update(id, request.body)
        Ok("updated user with id = " + id)
      }
    }
  }

  def deleteUser(id: Long) = authAction.async { implicit request =>
    service.find(id).map {
      case None => NotFound("user with id = " + id + " is not found")
      case Some(_) => {
        service.delete(id)
        Ok("user with id = " + id + " is deleted")
      }
    }
  }

  def getUsers = authAction.async { implicit request =>
    service.list().map { users =>
      Ok(Json.toJson(users))
    }
  }

  def getUser(id: Long) = authAction.async { implicit request =>
    service.find(id).map { users =>
      Ok(Json.toJson(users))
    }
  }

  def getUsernames = authAction.async { implicit request => 
    service.usernames().map { usernames => 
      Ok(Json.toJson(usernames))
    }
  }
}