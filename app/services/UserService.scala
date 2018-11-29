package services

import models._
import daos._
import javax.inject.{ Inject, Singleton }
import play.api.Logger

import scala.concurrent.{ Future, ExecutionContext }

@Singleton
class UserService @Inject() (repo: UserRepository) (implicit ec: ExecutionContext) {
  
  def create(user: User) = {
    Logger.debug("Creating new user, " + user.username)  
    repo.create(user)
  }

  def list() = repo.list

  def exists(id: Long) = repo.exists(id)

  def find(id: Long) = repo.find(id)

  def delete(id: Long) = repo.delete(id)

  def deleteAll() = repo.deleteAll

  def update(id: Long, user: User) = repo.update(id, user)

  def usernames() = repo.usernames
}
