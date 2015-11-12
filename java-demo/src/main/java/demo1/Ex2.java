package demo1;

import java.util.stream.IntStream;

import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.RunnableGraph;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import scala.concurrent.Future;
import scala.runtime.BoxedUnit;

public class Ex2 {
  public static void main(String[] args) {
    ActorSystem system = ActorSystem.create("demo1");
    ActorMaterializer materializer = ActorMaterializer.create(system);

    // Create a source with some integer and print them
    IntStream stream = IntStream.range(1, 4);
    Source<Integer, ?> source = Source.from(() -> stream.iterator());
    Flow<Integer, Integer, ?> f = Flow.<Integer> create().map(x -> x + 10).filter(x -> x % 2 == 0);
    Sink<Integer, Future<BoxedUnit>> sink = Sink.foreach(x -> System.out.println(x));
    RunnableGraph<?> runnable = source.via(f).to(sink);
    runnable.run(materializer);
  }
}
