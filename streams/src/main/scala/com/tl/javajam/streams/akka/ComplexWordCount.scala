package com.tl.javajam.streams.akka

import java.nio.file.Paths

import akka.actor.ActorSystem
import akka.{NotUsed, stream}
import akka.stream.scaladsl.{FileIO, Flow, Sink, Source, _}
import akka.stream.{ActorMaterializer, ClosedShape, Graph, IOResult}
import akka.util.ByteString

import scala.concurrent.{ExecutionContext, Future}


object ComplexWordCount extends App {
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
    .fold[Map[String, Int]](Map()){
    (wordCountMap, word) => wordCountMap.updated(word, wordCountMap.withDefaultValue(1)(word) + 1)


  }
  val wordCounterSink = Sink.foreach[Map[String,Int]](_.toSeq.sortBy(-1*_._2).take(10).foreach(println))

  val wordsToRemove = Seq("and","the","to","of","her","in")
  val cleaner = Flow[String].map(l => wordsToRemove.foldLeft(l)(
    (cleaned, word) => cleaned.replaceAll(s"$word\\b","")))

  val maxLenghtSink = Sink.reduce[String](
    (l1, l2) => if (l1.length > l2.length) l1 else l2)
    .mapMaterializedValue(_.foreach(println))

  val graph = GraphDSL.create() { implicit b =>
    import GraphDSL.Implicits._
    val broadcast = b.add(Broadcast[String](2))
    source ~> broadcast.in
    broadcast.out(0) ~> wordSplitter ~> wordCounterSink
    broadcast.out(1) ~> cleaner ~> maxLenghtSink
    ClosedShape
  }
  RunnableGraph.fromGraph(graph).run()(materializer)

}
