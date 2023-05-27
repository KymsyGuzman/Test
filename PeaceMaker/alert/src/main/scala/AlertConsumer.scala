package alert

import java.util.Properties
import java.time.Duration
import org.apache.kafka.common.serialization.StringSerializer
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.clients.consumer.{KafkaConsumer, ConsumerConfig}
import scala.collection.JavaConverters._
import play.api.libs.json._
import org.apache.kafka.clients.producer._

object Main {

  def main(args: Array[String]): Unit = {

    val propsConsumer = new Properties()
    propsConsumer.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    propsConsumer.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer])
    propsConsumer.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer])
    propsConsumer.put(ConsumerConfig.GROUP_ID_CONFIG, "alert_consumer")
    val consumer = new KafkaConsumer[String, String](propsConsumer)

    val propsProducer = new Properties()
    propsProducer.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    propsProducer.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer])
    propsProducer.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer])
    val producer = new KafkaProducer[String, String](propsProducer)

    val topic = "peacestate"
    consumer.subscribe(java.util.Collections.singletonList(topic))

    while(true) {
      val records = consumer.poll(Duration.ofMillis(10000)).asScala
      for (record <- records) {
        val json = Json.parse(record.value())
        Json.fromJson[Event](json).asOpt.foreach(event => {
          val dangerousPersons = event.persons.filter(person => person.peacescore < 0.2)
          dangerousPersons.foreach(person => println(s"[ALERT] ${person.name} is dangerous with ${person.peacescore} as peacescore."))
          if (dangerousPersons.nonEmpty) {
            val newAlert = Alert(
              event.peacewatcher_id,
              event.timestamp,
              Location(event.location.latitude, event.location.longitude),
              event.words,
              event.persons.filter(person => person.peacescore < 0.2),
              event.battery,
              event.temperature
            )
            val alertJsonString = Json.stringify(Json.toJson(newAlert))
            producer.send(new ProducerRecord[String, String]("alert", "event_alert", alertJsonString))

            // Send email
            val to = "marouaboudouk@gmail.com"
            val subject = "Dangerous Person Detected"
            val body = s"Dangerous person detected: $alertJsonString"
            EmailSender.sendEmail(to, subject, body)
          }
        })
      }
    }
    consumer.close()
  }
}
