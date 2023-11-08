package models.repos

import models.domains._
import javax.inject._
import play.api.db.slick.HasDatabaseConfigProvider
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import scala.concurrent.{ExecutionContext, Future}
import java.util.UUID

// case class Contact(
//   val id: UUID = UUID.randomUUID(),
//   val firstName: String = "",
//   val middleName: String = "", 
//   val lastName: String ="",
// )

@Singleton
class ContactRepo @Inject() 
(protected val dbConfigProvider: DatabaseConfigProvider)
(implicit executionContext: ExecutionContext) 
extends HasDatabaseConfigProvider[JdbcProfile]{
  import profile.api._

  protected class ContactTable(tag: Tag) extends Table[Contact](tag, "CONTACT"){
    def id = column[UUID]("ID", O.PrimaryKey)
    def firstName = column[String]("FIRST_NAME", O.Length(255))
    def middleName = column[String]("MIDDLE_NAME", O.Length(255))
    def lastName = column[String]("LAST_NAME", O.Length(255))
    def phoneNumber = column[String]("PHONE_NUMBER", O.Length(255))
    def email = column[String]("EMAIL", O.Length(255))
    
    def * = (id, firstName, middleName, lastName, phoneNumber, email).mapTo[Contact]
  }

  lazy val ContactTable = TableQuery[ContactTable]
  def createContactSchema = ContactTable.schema.createIfNotExists

  def all() = 
    db.run(ContactTable.result)

  def getContact(targetContact: UUID) = 
    db.run(ContactTable.filter(_.id === targetContact).result.headOption)

  def insert(newContact: Contact) =
    db.run(ContactTable+= newContact)

  def deleteContact(targetContact: UUID) = 
    db.run(ContactTable.filter(_.id === targetContact).delete)

  def updateContact(targetContact: UUID, newContact: Contact) = 
    db.run(ContactTable.filter(_.id === targetContact).update(newContact))


}
