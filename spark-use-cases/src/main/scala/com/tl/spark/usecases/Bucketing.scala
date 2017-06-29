package com.tl.spark.usecases

import org.apache.spark.sql.SparkSession

import scala.io.{Source, StdIn}
import scala.reflect.io.{Directory, File}

object Bucketing extends App {
  case class UserActivity(userId: Long, activity: String)
  val spark = SparkSession
    .builder()
    .appName("Bucketing use case")
    .master("local[4]")
    .getOrCreate()

  private val numOfUsers = 100000

  val activities = spark.sparkContext.range(1,numOfUsers,1).map(i =>
    UserActivity(i %10, s"""{"user": ${i%10}, activity: "msg num $i" }"""))


  val buckets_rdd = activities.map(a => (a.userId, a.activity + "\n"))
    .reduceByKey(_ + _)

  val folder = Directory.makeTemp("userActivities")
  println(folder)

  buckets_rdd.foreach {
    case (userId, userActivities) => //write to file
      File(folder.toAbsolute.toCanonical / s"$userId.txt").printlnAll(userActivities)
//      println(s"activities of user $userId\n${userActivities.take(1000)}")
  }

//  StdIn.readLine()
//  folder.deleteRecursively()




















  activities.groupBy(_.userId).foreach {
    case (userId, userActivities) =>
      //writing to file
      println(s"activities of user $userId")
      File(folder.toAbsolute.toCanonical / s"$userId.2.txt").printlnAll(userActivities.map(_.activity).toSeq: _*)

  }
  StdIn.readLine()
  folder.deleteRecursively()

}
