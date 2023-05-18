package Simulator

import java.util.Properties
import play.api.libs.json._
import java.time.LocalDateTime
import scala.util.Random
import scala.io.Source
import org.apache.kafka.clients.producer._
import org.apache.kafka.common.serialization.StringSerializer
import org.apache.log4j.{Level, Logger}

object Main {
  def generateEventData(names: JsArray, keywords: List[String], producer: KafkaProducer[String, String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val namesCount = names.value.size

    (1 to 1000).foreach { _ =>
      val event = Event(
        Random.nextInt(100000 - 10000 + 1) + 10000,
        LocalDateTime.now(),
        Coordonnees(-50.0 + (50.0 - (-50.0)) * Random.nextDouble(), -50.0 + (50.0 - (-50.0)) * Random.nextDouble()),
        (1 to (Random.nextInt(5 - 1 + 1) + 1)).map(_ => keywords(Random.nextInt(keywords.size))).toList,
        (1 to (Random.nextInt(10 - 1 + 1) + 1)).map(_ =>
          Person(
            (names.value(Random.nextInt(namesCount)).as[JsObject] \ "name").as[String],
            Random.nextDouble
          )
        ).toList,
        Random.nextInt(100 - 0 + 1) + 0,
        Random.nextInt(50 - (-30) + 1) + (-30)
      )

      val eventJsonString = Json.stringify(Json.toJson(event))
      println(eventJsonString)

      val record = new ProducerRecord[String, String]("peacestate", "event", eventJsonString)
      producer.send(record)
    }

    producer.flush()
  }

  def loadNamesFromJsonFile(fileName: String): JsArray = {
    val file = new java.io.File(fileName)
    if (!file.exists()) {
      throw new IllegalArgumentException(s"File $fileName does not exist.")
    }

    val namesRaw = Source.fromFile(file).getLines.mkString
    Json.parse(namesRaw).as[JsArray]
  }

  def createKafkaProducer(): KafkaProducer[String, String] = {
    val props = new Properties()
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)

    new KafkaProducer[String, String](props)
  }
 
  def main(args: Array[String]): Unit = {
    val namesFileName = "/home/kymsy/Documents/m1bdml/dataengarchi/PeaceWatcher/PeaceMaker/Simulator/src/main/resources/names.json"
    val keywords = List("sad", "angry", "unhappy", "not in peace")

    try {
      val namesJson = loadNamesFromJsonFile(namesFileName)
      val producer = createKafkaProducer()

      generateEventData(namesJson, keywords, producer)

      producer.close()
    } catch {
      case e: Throwable =>
        println(s"An error occurred: ${e.getMessage}")
    }
  }
}
