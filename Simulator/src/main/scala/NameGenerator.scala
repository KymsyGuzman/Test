package Simulator

import java.io.FileWriter
import play.api.libs.json._

object NameGenerator {
  final case class NameData(year: Int, name: String, percent: Double, sex: String)

  object NameData {
    implicit val nameDataFormat: OFormat[NameData] = Json.format[NameData]
  }

  def generateNamesData(): Seq[NameData] = {
    val random = new scala.util.Random()
    val sexes = Seq("M", "F")
    val names = Seq("John", "Emma", "Michael", "Sophia", "William", "Olivia", "James", "Ava")

    (1 to 1000).map { _ =>
      val year = random.nextInt(2020 - 2000 + 1) + 2000
      val name = names(random.nextInt(names.length))
      val percent = random.nextDouble()
      val sex = sexes(random.nextInt(sexes.length))

      NameData(year, name, percent, sex)
    }
  }

  def main(args: Array[String]): Unit = {
    val namesData = generateNamesData()
    val json = Json.toJson(namesData)

    val filePath = "/home/kymsy/Documents/m1bdml/dataengarchi/PeaceWatcher/PeaceMaker/Simulator/src/main/resources/names.json"

    val fileWriter = new FileWriter(filePath)
    try {
      fileWriter.write(Json.prettyPrint(json))
    } finally {
      fileWriter.close()
    }

    println(s"names.json generated successfully at: $filePath")
  }
}