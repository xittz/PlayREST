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

  private class UserTable(tag: Tag) extends Table[User](tag, "users") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def username = column[String]("username")
    def password = column[String]("password")
    def isDeleted = column[Boolean]("isDeleted")

    def * = (id, username, password, isDeleted) <> ((User.apply _).tupled, User.unapply)
  }

  private val users = TableQuery[UserTable]
 
  def create(user: User): Future[User] = db.run {
    (users returning users.map(_.id)
      into ((user, id) => user.copy(id = id))
    ) += user
  }

  def list(): Future[Seq[User]] = db.run {
    users.result
  }

  def exists(id: Long): Future[Boolean] = db.run {
    users.filter(_.id === id).exists.result
  }

  def find(id: Long): Future[Option[User]] = db.run {
    users.filter(_.id === id).result.headOption
  }

  def delete(id: Long) = db.run {
    users.filter(_.id === id).map(_.isDeleted).update(true)
  }

  def update(id: Long, user: User) = db.run {
    val newUser = user.copy(id = id)
    users.filter(_.id === id).update(newUser)
  }

  def usernames(): Future[Seq[String]] = db.run {
    users.map(_.username).result
  }
}
