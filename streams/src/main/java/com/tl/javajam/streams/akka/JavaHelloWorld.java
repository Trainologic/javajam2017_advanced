package com.tl.javajam.streams.akka;


import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.stream.*;
import akka.stream.javadsl.*;

public class JavaHelloWorld {
    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.apply("JavaHelloWorld");
        Materializer materializer = ActorMaterializer.create(actorSystem);
        final Source<Integer, NotUsed> source = Source.range(1, 100);
        source.runForeach(i -> System.out.println(i), materializer)
                .thenRun(actorSystem::terminate);

    }
}
