package models.domains

import java.util.UUID
import play.api.libs.json._

case class Contact(
  val id: UUID = UUID.randomUUID(),
  val firstName: String = "",
  val middleName: String = "", 
  val lastName: String ="" ,
  val phoneNumber: String = "",
  val email: String = "",
)

object Contact {
  implicit val format: Format[Contact] = Json.format[Contact]
}