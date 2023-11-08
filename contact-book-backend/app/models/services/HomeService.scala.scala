package models.services

import models.repos._
import models.domains._
import javax.inject._
import play.api.db.slick.HasDatabaseConfigProvider
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import scala.concurrent.{ExecutionContext, Future}
import java.util.UUID

@Singleton
class HomeService @Inject()(
  protected val dbConfigProvider: DatabaseConfigProvider, 
  val contactRepo: ContactRepo
)
(implicit executionContext: ExecutionContext)
extends HasDatabaseConfigProvider[JdbcProfile]{
  import profile.api._
    def createSchemas ={ 
      db.run(
        contactRepo.createContactSchema
      )
    }

    def getAllContacts =
      contactRepo.all()

    def getContact(targetContact: UUID) =
      contactRepo.getContact(targetContact)
      
    def addContact(newContact: Contact) =
      contactRepo.insert(newContact)

    def updateContact(targetContact: UUID, newContact: Contact) = 
      contactRepo.updateContact(targetContact, newContact)

    def deleteContact(targetContact: UUID) = 
      contactRepo.deleteContact(targetContact)
}