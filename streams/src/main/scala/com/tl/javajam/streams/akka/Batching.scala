package com.tl.javajam.streams.akka

import java.time.LocalTime

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ThrottleMode}
import akka.stream.scaladsl.Source

import scala.collection.immutable.Seq
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

object Batching extends App {
  implicit val system = ActorSystem("HelloWorld")
  implicit val executionContext: ExecutionContext = system.dispatcher
  val materializer = ActorMaterializer()

  def doAggregativeCalc(numbers: Seq[Int]): Unit = {
    println(s"got $numbers with the sum ${numbers.sum} - ${LocalTime.now()}")
  }


  Source(1 to 100)
    .batch(10, x => Seq(x))((numbers,n) => numbers.+:(n))
    .throttle(3,1 second,1, ThrottleMode.Shaping)
    .runForeach(doAggregativeCalc)(materializer)
    .onComplete(_ => system.terminate())
}
