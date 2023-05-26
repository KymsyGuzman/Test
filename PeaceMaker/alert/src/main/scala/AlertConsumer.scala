package alert

import java.util.Properties
import play.api.libs.json._
import org.apache.kafka.common.serialization.{StringDeserializer, StringSerializer}
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.LocationStrategies._
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.SparkConf

import org.apache.log4j.{Level, Logger}

object Main {

  def main(args: Array[String]): Unit = {
    // Keep only the errors
    Logger.getLogger("org").setLevel(Level.ERROR)

    val sparkConf = new SparkConf()
      .setAppName("peaceland_alert")
      .setMaster("local[*]")
      .set("spark.driver.host", "127.0.0.1")

    val ssc = new StreamingContext(sparkConf, Seconds(5))

    val kafkaParams = Map(
      "bootstrap.servers" -> "localhost:9092",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "alert_consumer"
    )

    val topics = Array("peacestate")

    val stream = KafkaUtils.createDirectStream[String, String](
      ssc,
      PreferConsistent,
      Subscribe[String, String](topics, kafkaParams)
    )

    val props = new Properties()
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)

    val sparkContext = ssc.sparkContext
    val kafkaSink = sparkContext.broadcast(KafkaSink(props))

    stream.flatMap(record => {
      val json = Json.parse(record.value())
      Json.fromJson[Event](json).asOpt
    }).filter(event => {
      val dangerousPersons = event.persons.filter(person => person.peacescore < 0.2)
      dangerousPersons.foreach(person => println(s"[ALERT] ${person.name} is dangerous with ${person.peacescore} as peacescore."))
      dangerousPersons.nonEmpty
    }).map({ event =>
      val newAlert = Alert(
        event.peacewatcher_id,
        event.timestamp,
        Location(event.location.latitude, event.location.longitude),
        event.words,
        event.persons.filter(person => person.peacescore < 0.2),
        event.battery,
        event.temperature
      )
      Json.stringify(Json.toJson(newAlert))
    }).foreachRDD({ rdd =>
      rdd.foreach({ alertJsonString =>
        kafkaSink.value.send("alert", "event_alert", alertJsonString)

        // Send email
        val to = "marouaboudouk@gmail.com"
        val subject = "Dangerous Person Detected"
        val body = s"Dangerous person detected: $alertJsonString"
        EmailSender.sendEmail(to, subject, body)
      })
    })

    ssc.start()
    ssc.awaitTermination()
  }
}