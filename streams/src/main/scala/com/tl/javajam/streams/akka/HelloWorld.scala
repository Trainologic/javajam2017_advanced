package com.tl.javajam.streams.akka

import akka.NotUsed
import akka.actor.ActorSystem

//Should always be imported
import akka.stream._
import akka.stream.scaladsl._

import scala.concurrent._


object HelloWorld extends App {
  implicit val system = ActorSystem("HelloWorld")
  implicit val executionContext: ExecutionContext = system.dispatcher
  val materializer = ActorMaterializer()

  val source: Source[Char, NotUsed] = Source("Hello World\n")
  val flow = Flow[Char].map(_.toUpper)
  val sink = Sink.foreach[Char](print)
  val graph = source.via(flow).toMat(sink)(Keep.right)
  graph.run()(materializer).onComplete(_ => system.terminate())
}


