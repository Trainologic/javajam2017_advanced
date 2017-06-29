package com.tl.spark.deepdive

import org.apache.spark.broadcast.Broadcast
import org.apache.spark.sql.SparkSession

import scala.io.{Source, StdIn}

object NoShuffle extends App {

  val spark = SparkSession
    .builder()
    .appName("Spark App")
    .master("local[4]")
    .getOrCreate()

  private val sc = spark.sparkContext
  //  val positive = sc.textFile("src/main/resources/positive.txt")
  val positiveWords = Source.fromFile("src/main/resources/positive.txt").getLines().toSet
  val positiveWordsBroadcast: Broadcast[Set[String]] = sc.broadcast(positiveWords)

  val bookRdd = sc.textFile("src/main/resources/books/*.txt")
  val numOfPositives = bookRdd
    .flatMap(_.toLowerCase.split(" "))
    .filter(positiveWordsBroadcast.value.contains)
    .count()

  println(s"got $numOfPositives words")
  StdIn.readLine()

}
