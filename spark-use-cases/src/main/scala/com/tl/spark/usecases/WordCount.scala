package com.tl.spark.usecases

import org.apache.spark.sql.SparkSession

import scala.io.StdIn


object WordCount extends App {
  val spark = SparkSession
    .builder()
    .appName("Word Count")
    .master("local[4]")
    .getOrCreate()

  private val sc = spark.sparkContext
  val counts =
    sc.textFile("src/main/resources/book.txt")
    .flatMap(line => line.split(" "))
    .map(w => (w,1))
    .reduceByKey((count1, count2) => count1 + count2)
  println(counts.toDebugString)

  println(counts.collect())

  StdIn.readLine("Press enter to exit\n")








}
