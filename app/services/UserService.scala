package services

import models._
import daos._
import javax.inject.{ Inject, Singleton }

import scala.concurrent.{ Future, ExecutionContext }

@Singleton
class UserService @Inject() (repo: UserRepository) (implicit ec: ExecutionContext) {
  
  def create(username: String, password: String) = repo.create(username, password)

  def list() = repo.list

  def exists(id: Long) = repo.exists(id)

  def find(id: Long) = repo.find(id)

  def delete(id: Long) = repo.delete(id)

  def update(id: Long, username: String, password: String) = repo.update(id, username, password)

  def usernames() = repo.usernames
}
