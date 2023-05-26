package Storage

import play.api.libs.json.{Json, OFormat}

final case class SaveableEvent(
                                peacewatcherID : Int,
                                timestamp: String,
                                location: Coordonnees,
                                words: List[String],
                                persons: List[Person],
                                battery: Int,
                                temperature: Int
                              )
object SaveableEvent {
  implicit val SaveableEventFormatter: OFormat[SaveableEvent] = Json.format[SaveableEvent]
}
