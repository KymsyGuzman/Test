package alert

import java.time.LocalDateTime
import play.api.libs.json.{Json, OFormat}

final case class Coordonnees(
                        latitude: Double,
                        longitude: Double
                        )
object Coordonnees {
    implicit val CoordsFormatter: OFormat[Coordonnees] = Json.format[Coordonnees]
}