package com.tl.spark

import java.time.LocalTime

import org.apache.spark.SparkConf
import org.apache.spark.streaming._
import org.apache.spark.streaming.dstream.DStream

import scala.io.StdIn
import scala.reflect.io.Directory
import scala.concurrent.ExecutionContext.Implicits.global


object Streaming extends App {
  val conf = new SparkConf()
    .setAppName("Streaming")
    .setMaster("local[4]")
  val folder = Directory.makeTemp("streaming")
  val checkpoint = Directory.makeTemp("checkpoint")

  val ssc = new StreamingContext(conf, Seconds(1))
  ssc.checkpoint(checkpoint.toString())
  val dstream: DStream[Int] = ssc.textFileStream(s"file://$folder/").map(_.trim.toInt)
//  printRdds(dstream)
//  printRddSums(dstream)
  printTotalSum(dstream)



  ssc.start()
  Helpers.createFiles(folder).onComplete(_ => {
    StdIn.readLine("finished,press ENTER\n")
    ssc.stop(stopSparkContext = false, stopGracefully = false)
  })
  ssc.awaitTermination()
  folder.delete()



  def printRdds(dStream: DStream[Int]) = dstream.print()
  def printRddSums(dStream: DStream[Int]) = dstream.reduce(_ + _).print()
  def printTotalSum(dStream: DStream[Int]) = {
    def updateFunction(newValues: Seq[Int], runningCount: Option[Int]): Option[Int] = {
      runningCount.map(_ + newValues.sum).orElse(Some(newValues.sum))
//      Some(runningCount.fold(newValues.sum) {_ + newValues.sum})
    }

    dstream.map(i => (i%3,i))
      .reduceByKey(_ + _)
      .updateStateByKey(updateFunction)
      .foreachRDD { r =>
        println(s"[${LocalTime.now()}] sum = ${r.values.sum()}")
      }
  }
}
