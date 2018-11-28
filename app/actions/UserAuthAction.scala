package actions

import models._
import javax.inject.{ Inject, Singleton }
import play.api.mvc._
import scala.concurrent.{ ExecutionContext, Future }


@Singleton
class UserAuthAction @Inject() (implicit val ec: ExecutionContext, playBodyParsers: PlayBodyParsers) extends ActionBuilder[Request, User] with Results {
  override def parser = playBodyParsers.json[User]
  override def executionContext = ec

  override def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]): Future[Result] = {
    request.headers.get("authToken").collect {
      case "123123" => block(request)
    } getOrElse {
      Future.successful(Forbidden("auth token is required"))
    }
  }
} 

class UserRequest[A](val user: User, request: Request[A]) extends WrappedRequest[A](request)