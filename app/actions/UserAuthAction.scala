package actions

import models._
import javax.inject.{ Inject, Singleton }
import play.api.mvc._
import play.api.Logger
import scala.concurrent.{ ExecutionContext, Future }


@Singleton
class UserAuthAction @Inject() (implicit val ec: ExecutionContext, playBodyParsers: PlayBodyParsers) extends ActionBuilder[Request, User] with Results {
  override def parser = playBodyParsers.json[User]
  override def executionContext = ec

  override def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]): Future[Result] = {
    request.headers.get("authToken").collect {
      case "123123" => {
        Logger.info("Connection with valid auth token")
        block(request)
      }
    } getOrElse {
      Logger.warn("Connection without an auth token")
      Future.successful(Forbidden("auth token is required"))
    }
  }
} 

class UserRequest[A](val user: User, request: Request[A]) extends WrappedRequest[A](request)