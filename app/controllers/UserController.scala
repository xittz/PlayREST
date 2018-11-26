package controllers

import javax.inject._

import models._
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.i18n._
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

case class CreateUserForm(username: String, password: String)

class UserController @Inject()(repo: UserRepository,
                                  cc: MessagesControllerComponents
                                )(implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {

  val userForm: Form[CreateUserForm] = Form {
    mapping(
      "username" -> nonEmptyText,
      "password" -> nonEmptyText
    )(CreateUserForm.apply)(CreateUserForm.unapply)
  }

  // adds a new user
  def addUser = Action.async { implicit request =>
    userForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok("Wasn't able to create a new user"))
      },
      user => {
        repo.create(user.username, user.password).map { _ =>
          Ok("User " + user.username + " is created")
        }
      }
    )
  }

  // returns list of all users
  def getUsers = Action.async { implicit request =>
    repo.list().map { users =>
      Ok(Json.toJson(users))
    }
  }

  def getUser(id: Long) = Action.async { implicit request =>
    repo.list().map { users =>
      Ok(Json.toJson(users.filter(_.id == id)))
    }
  }

  // deletes a user with `id` == id
  def deleteUser(id: Long) = Action.async { implicit request => 
    repo.delete(id).map { _ => 
      Ok("deleted user with id = " + id)
    }
  }

  // updates a user with `id` == id
  def updateUser(id: Long, username: String, password: String) = Action { implicit request =>
    Ok("updated")
    /*
    repo.update(id, username, password).map { _ => 
      Ok("updated user with id = " + id)
    }*/
  }

  // returns a list of all usernames
  def getUsernames = Action.async { implicit request => 
    repo.usernames().map { usernames => 
      Ok(Json.toJson(usernames))
    }
  }

}