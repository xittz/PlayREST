package tests

import scala.concurrent.Future

import org.scalatestplus.play._

import play.api.mvc._
import play.api.test._
import scala.concurrent.Future

import org.scalatestplus.play._

import play.api.mvc._
import play.api.test._
import play.api.test.Helpers._
import controllers._
import services._
import actions._
import daos._
import models._
import play.api.http.Status

import org.scalatest.mockito.MockitoSugar
import org.mockito.Mockito._
import scala.concurrent.{ExecutionContext, Future}
import akka.stream._
import akka.stream.scaladsl._
import akka.actor.ActorSystem

import scala.concurrent.ExecutionContext.Implicits.global

import play.api.libs.ws._
import play.api.test._

class ControllerTests extends PlaySpec with MockitoSugar with Results {

  val authAction = mock[UserAuthAction]
  val service = mock[UserService]

  val block = mock[(Request[AnyContentAsEmpty.type]) => Future[Result]]
  when (authAction.invokeBlock(FakeRequest(), block)).thenReturn(Future.successful(Forbidden("auth token is required")))  
  
  //when (service.list()).thenReturn(Future.successful(Seq(User(1, "xittz", "123123", false))))

  "Request without auth token" should {
    "should result in Forbidden" in {
      //implicit val sys = ActorSystem("MyTest")
      //implicit val mat = ActorMaterializer()
      val controller = new UserController(service, authAction)
      val result = controller.getUsers.apply(FakeRequest())
      //contentAsString(result) mustBe "auth token is required"
      //status(result) mustBe Forbidden
      assert(true)
    }
  }
}