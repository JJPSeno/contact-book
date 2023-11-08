package controllers

import models.services.HomeService
import models.domains._
import javax.inject._
import play.api._
import play.api.mvc._
import java.util.UUID
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import scala.concurrent.{ExecutionContext, Future}

final case class ContactFormClass(
  firstName: String,
  middleName: String,
  lastName: String,
  phoneNumber: String,
  email: String,
) 

val contactForm: Form[ContactFormClass] = Form(mapping(
  "firstName"-> nonEmptyText, 
  "middleName" -> nonEmptyText,
  "lastName" -> nonEmptyText,
  "phoneNumber" -> default(text, ""),
  "email" -> default(email, "")
  )
(ContactFormClass.apply)((contForm: ContactFormClass) => Some(contForm.firstName, contForm.middleName, contForm.lastName, contForm.phoneNumber, contForm.email)))

val updateContactForm: Form[Contact] = Form(mapping(
  "id" -> uuid,
  "firstName"-> nonEmptyText, 
  "middleName" -> nonEmptyText,
  "lastName" -> nonEmptyText,
  "phoneNumber" -> default(text, ""),
  "email" -> default(email, "")
  )
(Contact.apply)((contact: Contact) => Some(contact.id, contact.firstName, contact.middleName, contact.lastName, contact.phoneNumber, contact.email)))


case class ContactToJson(
  id: UUID,
  firstName: String,
  middleName: String,
  lastName: String,
  phoneNumber: String,
  email: String,
)

implicit val contactWrites: Writes[ContactToJson] = 
  new Writes[ContactToJson]{
    def writes(contact: ContactToJson) = Json.obj(
      "id" -> contact.id,
      "firstName"  -> contact.firstName,
      "middleName"  -> contact.middleName,
      "lastName"  -> contact.lastName,
      "phoneNumber"  -> contact.phoneNumber,
      "email"  -> contact.email,
    )
  }

implicit val contactReads: Reads[Contact] = (
(JsPath \ "id").read[UUID] and
  (JsPath \ "firstName").read[String] and
  (JsPath \ "middleName").read[String] and 
  (JsPath \ "lastName").read[String] and 
  (JsPath \ "phoneNumber").read[String] and 
  (JsPath \ "email").read[String]
)(Contact.apply _)

/**
## Contact Book App
Contact Book App: The app allows users to manage their contacts by viewing, adding, deleting, and updating them. 
Users can add new contacts, delete existing ones, or update their details. When adding a new contact, users must provide 
at least one of the following: a first name, a middle name, or a last name, along with one additional 
piece of contact information such as a phone number or email. 
Optionally, each contact can be associated with a group for easier searching and organization. 
The contact list displays all saved contacts, complete with their details, and offers options to delete or 
update each entry. The app uses play framework 3 (Scala 3) for the api, Postgres for database and VueJs 3 for client pages.
 */
@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents, val homeService: HomeService)
(implicit executionContext: ExecutionContext) extends BaseController {

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def init() = Action.async{ implicit request: Request[AnyContent] =>
    homeService.createSchemas.map{_=>Ok("db initialied")}
  }

  def fetchContactData() = Action.async{ implicit request: Request[AnyContent] =>
    homeService.getAllContacts.map{contacts => 
        Ok(Json.toJson(contacts))
      }
  }
  

  // POST    /contact/add                controllers.HomeController.addContact()
  // POST    /contact/update/:id         controllers.HomeController.updateContact(id: UUID)
  // POST    /contact/delete/:id         controllers.HomeController.deleteContact(id: UUID)

  def addContact() = 
    Action.async { implicit request =>
    contactForm.bindFromRequest().fold(
      formWithErrors => {
        Future.successful(BadRequest("Please try again. Errors: " + formWithErrors.errors.mkString("\n")))
      },
      contact => {
        if (contact.phoneNumber.isEmpty && contact.email.isEmpty) {
          Future.successful(BadRequest("Phone number or email is required."))
        }
        else {
          val newContact = Contact(
            firstName = contact.firstName,
            middleName = contact.middleName,
            lastName = contact.lastName,
            phoneNumber = contact.phoneNumber,
            email = contact.email
          )
          homeService.addContact(newContact).map(_ => Ok("Contact Saved"))
        }
      })
    }
  
  def getContact(id: UUID) =
    Action.async { implicit request =>
      homeService.getContact(id).map { 
        case Some(contact) => 
          Ok(
            Json.toJson(
              ContactToJson(
                contact.id,
                contact.firstName, 
                contact.middleName, 
                contact.lastName, 
                contact.phoneNumber, 
                contact.email
              )
            ) 
          )
        case None => BadRequest("Contact does not exist")
      }
    }
  
  def updateContact() =
    Action.async { implicit request =>
    updateContactForm.bindFromRequest().fold(
      formWithErrors => {
        Future.successful(BadRequest("Please try again. Errors: " + formWithErrors.errors.mkString("\n")))
      },
      updatedContact => {
        if (updatedContact.phoneNumber.isEmpty && updatedContact.email.isEmpty) {
          Future.successful(BadRequest("Phone number or email is required."))
        }
        else {
          homeService.updateContact(updatedContact.id, updatedContact).map{
            case 1 => Ok("Contact updated")
            case 0 => BadRequest("Contact does not exist")
          }
        }
      })
    }

  def deleteContact(id: UUID) =
    Action.async { implicit request =>
      homeService.deleteContact(id).map { 
        case 1 => Ok("Contact deleted")
        case 0 => BadRequest("Contact does not exist")
      }
    }

}
