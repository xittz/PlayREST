package daos

import models._
import javax.inject.{ Inject, Singleton }
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import play.api.libs.json._
import play.api.libs.functional.syntax._

import scala.concurrent.{ Future, ExecutionContext }

@Singleton
class UserRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private class UserTable(tag: Tag) extends Table[UserInfo](tag, "users") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def username = column[String]("username")
    def password = column[String]("password")

    def * = (id, username, password) <> ((UserInfo.apply _).tupled, UserInfo.unapply)
  }

  private val users = TableQuery[UserTable]
 
  def create(username: String, password: String): Future[UserInfo] = db.run {
    (users.map(u => (u.username, u.password))
      returning users.map(_.id)
      into ((userInfo, id) => UserInfo(id, userInfo._1, userInfo._2))
    ) += (username, password)
  }

  def list(): Future[Seq[UserInfo]] = db.run {
    users.result
  }

  def exists(id: Long): Future[Boolean] = db.run {
    users.filter(_.id === id).exists.result
  }

  def find(id: Long): Future[Option[UserInfo]] = db.run {
    users.filter(_.id === id).result.headOption
  }

  def delete(id: Long) = db.run {
    users.filter(_.id === id).delete
  }

  def update(id: Long, username: String, password: String) = db.run {
    val updatedUser = UserInfo(id, username, password)
    users.filter(_.id === id).update(updatedUser)
  }

  def usernames(): Future[Seq[String]] = db.run {
    users.map(_.username).result
  }
}
