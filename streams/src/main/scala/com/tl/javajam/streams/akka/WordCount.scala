package com.tl.javajam.streams.akka

import java.nio.file.{Path, Paths}

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, IOResult}
import akka.stream.scaladsl.{FileIO, Flow, Sink, Source}
import akka.util.ByteString
import akka.stream._
import akka.stream.scaladsl._

import scala.concurrent.{ExecutionContext, Future}


object WordCount extends App {
  implicit val system = ActorSystem("akka-streams")
  implicit val ec: ExecutionContext = system.dispatcher
  implicit val materializer = ActorMaterializer()

  val source: Source[String, Future[IOResult]] =
    FileIO.fromPath(Paths.get("src/main/resources/book.txt"))
      .via(Framing.delimiter(ByteString("\n"), maximumFrameLength = 100000, allowTruncation = true))
      .map(_.utf8String)

  val wordSplitter = Flow[String]
    .mapConcat(_.split(" ").to)
    .filter(_.length > 1)

  val wordCounterSink = Sink.fold[Map[String, Int], String](Map()){
    (wordCountMap, word) => wordCountMap.updated(word, wordCountMap.withDefaultValue(1)(word) + 1)
  }

  private val result: Future[Map[String, Int]] = source.via(wordSplitter).runWith(wordCounterSink)
  result.onComplete(attempt => {
    attempt.foreach(r =>println(r.toSeq.sortBy(-1 * _._2).take(10)))
    system.terminate()
  })
}
