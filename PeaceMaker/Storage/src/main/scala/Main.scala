package Storage

import play.api.libs.json._

import java.io.FileNotFoundException
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter 

import scala.io.Source

import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.clients.consumer.ConsumerConfig

import org.apache.spark.sql.{SparkSession, DataFrame, SaveMode, Row, SQLContext}

import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.LocationStrategies._
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.Encoders
import org.apache.spark.sql.SQLContext

import scala.reflect.ClassTag

import org.apache.log4j.{Level, Logger}


object Main extends App {
  // keep only the errors
  Logger.getLogger("org").setLevel(Level.ERROR)

  val sparkConf = new SparkConf()
    .setMaster("local[*]")
    .setAppName("save_aggregate")
    .set("spark.driver.host", "127.0.0.1")

  val streamContext = new StreamingContext(sparkConf, Seconds(15))
  val spark: SparkSession = SparkSession.builder.config(streamContext.sparkContext.getConf).getOrCreate()

  val kafkaParams = Map(
    "bootstrap.servers" -> "localhost:9092",
    "key.deserializer" -> classOf[StringDeserializer],
    "value.deserializer" -> classOf[StringDeserializer],
    "group.id" -> "aggregate"
  )

  val topics = Array("peacestate")

  val stream = KafkaUtils.createDirectStream[String, String](
    streamContext,
    PreferConsistent,
    Subscribe[String, String](topics, kafkaParams)
  )

  // Process and transform the incoming stream of events
  stream.flatMap(record => {
      val json = Json.parse(record.value())
      Event.EventFormatter.reads(json).asOpt
    })
    .map(event => {
      println(event)
      val serializedTime = event.timestamp.format(DateTimeFormatter.ISO_DATE_TIME)
      SaveableEvent(event.peacewatcher_id, serializedTime, event.location, event.words, event.persons, event.battery, event.temperature)
    })
    .foreachRDD { rdd =>
      import spark.implicits._

      val eventsDF = rdd.toDF()
      eventsDF.write.mode(SaveMode.Append).parquet("/home/kymsy/output/")
    }

  streamContext.start()
  streamContext.awaitTermination()
}
