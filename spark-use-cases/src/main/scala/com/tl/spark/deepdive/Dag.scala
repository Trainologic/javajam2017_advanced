package com.tl.spark.deepdive

import org.apache.spark.sql.SparkSession
import org.apache.spark.storage.StorageLevel

import scala.io.{Source, StdIn}

object Dag extends App {
  val spark = SparkSession
    .builder()
    .appName("Spark App")
    .master("local[4]")
    .config("spark.logLineage","true")
    .getOrCreate()

  val sc = spark.sparkContext

  val positive = sc.textFile("src/main/resources/positive.txt")
    .map(w => (w,1))
  val bookRdd = sc.textFile("src/main/resources/books/*.txt")
    .flatMap(_.toLowerCase.split(" "))
    .map(w => (w,1))
  val onlyPositive = bookRdd.join(positive)

  println(onlyPositive.toDebugString)

  val nPositive = onlyPositive.count()

  println(s"found $nPositive")
  StdIn.readLine()


}
