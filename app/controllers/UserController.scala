package controllers

import javax.inject._

import services._
import models._
import actions._
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

class UserController @Inject()(
  service: UserService,
  authAction: UserAuthAction,
  cc: MessagesControllerComponents)(implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {

  def ItemAction(id: Long)(implicit ec: ExecutionContext) = new ActionRefiner[Request, UserRequest] {
    def executionContext = ec
    def refine[A](input: Request[A]) = {
      service.find(id).map {
        case None => Left(NotFound)
        case Some(user) => Right(new UserRequest(user, input))
      }
    }
  }

  def CheckIfDeletedAction(implicit ec: ExecutionContext) = new ActionFilter[UserRequest] {
    def executionContext = ec
    def filter[A](input: UserRequest[A]) = Future.successful {
      if (input.user.isDeleted) Some(NotFound) else None
    }
  }

  def addUser = authAction.async { implicit request => 
    service.create(request.body).map { _ => 
      Created("user " + request.body.username + " is created")
    }
  }

  def updateUser(id: Long) = (authAction andThen ItemAction(id)).async { implicit request =>
    service.update(id, request.body).map { _ => 
      Ok("user with id = " + request.user.id + " is updated ")
    }
  }

  def deleteUser(id: Long) = (authAction andThen ItemAction(id) andThen CheckIfDeletedAction).async { implicit request =>
    service.delete(id).map { _ => 
      Ok("user with id = " + id + " is deleted")
    }
  }

  def getUsers = authAction.async(parse.raw) { implicit request =>
    service.list().map { users =>
      Ok(Json.toJson(users))
    }
  }

  def getUser(id: Long) = (authAction andThen ItemAction(id))(parse.raw) { implicit request =>
    Ok(Json.toJson(request.user))
  }

  def getUsernames = authAction.async(parse.raw) { implicit request => 
    service.usernames().map { usernames => 
      Ok(Json.toJson(usernames))
    }
  }
}