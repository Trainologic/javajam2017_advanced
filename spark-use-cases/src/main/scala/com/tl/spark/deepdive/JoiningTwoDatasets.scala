package com.tl.spark.deepdive

import com.tl.spark.deepdive.Dag.sc
import org.apache.spark.sql.{Dataset, SparkSession}

import scala.io.StdIn

/**
  * Created by alexlanda on 20/06/2017.
  */
object JoiningTwoDatasets extends App {
  case class UserActivity(userId: Long, timestamp: Long, activity: String)
  val spark = SparkSession
    .builder()
    .appName("Joining two datasets use case")
    .master("local[4]")
    .getOrCreate()

  import spark.implicits._

  val positive: Dataset[String] = spark.read.textFile("src/main/resources/positive.txt")
  val books: Dataset[String] = spark.read.textFile("src/main/resources/books/*.txt")
    .flatMap(_.toLowerCase.split(" "))
//  spark.read.format()
  books.printSchema()
  positive.printSchema()
  println(books.join(positive,"value").count())

  StdIn.readLine()








}
