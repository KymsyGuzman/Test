package Simulator

import java.time.LocalDateTime
import play.api.libs.json.{Json, OFormat}

final case class Event(
                        peacewatcher_id : Int, 
                        timestamp: LocalDateTime,
                        location: Coordonnees,
                        words: List[String],
                        persons: List[Person],
                        battery: Int,
                        temperature: Int
                    )
object Event {
    implicit val EventFormatter: OFormat[Event] = Json.format[Event]
}
