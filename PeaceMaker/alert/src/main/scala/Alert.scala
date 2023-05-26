package alert

import java.time.LocalDateTime
import play.api.libs.json.{Json, OFormat}
final case class Alert(
                        peacewatcher_id: Int,
                        timestamp: LocalDateTime,
                        location: Location,
                        words: List[String],
                        persons: List[Person],
                        battery: Int,
                        temperature: Int,
                    )

object Alert {
    implicit val AlertFormatter: OFormat[Alert] = Json.format[Alert]
}