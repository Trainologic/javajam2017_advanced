package com.tl.spark.usecases

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

import scala.io.StdIn
import scala.util.Random

object TopActiveUsers extends App {



  case class UserActivity(userId: Long, activity: String)

  val spark = SparkSession
    .builder()
    .appName("TopActiveUsers")
    .master("local[4]")
    .getOrCreate()

  private val numOfUsers = 100000000

  val activities = spark.sparkContext.range(1, numOfUsers, 1).map{i =>
    val userId = Random.nextInt(numOfUsers) % 100
    UserActivity(userId, "foobar")
  }

  val userActivities: RDD[(Long, Int)] = activities.map(u => (u.userId,1))
    .reduceByKey(_ +_ )
    .cache()
  println(s"we have ${userActivities.count()} active users")

  private val topUsers = userActivities.map(userCount => (userCount._2, userCount._1))
    .sortByKey(ascending = false)
    .take(10).map(_._2)
  println(s"Top users sorted: ${topUsers.mkString("\t")}")












    private val topUsersTaken = userActivities.takeOrdered(10)(Ordering.by[(Long,Int),Int](_._2).reverse)
      .map(_._1)
    println(s"Top users taken:  ${topUsersTaken.mkString("\t")}")

  StdIn.readLine()


}
