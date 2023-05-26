package alert

import java.time.LocalDateTime
import play.api.libs.json.{Json, OFormat}
final case class Location(lat: Double, lon: Double)

object Location {
    implicit val LocationFormatter: OFormat[Location] = Json.format[Location]
}