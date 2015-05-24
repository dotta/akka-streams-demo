package demo1;

import java.util.stream.IntStream;

import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import scala.runtime.BoxedUnit;
import akka.actor.ActorSystem;
import akka.japi.function.Function2;
import akka.stream.ActorFlowMaterializer;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;

public class Ex3 {
  public static void main(String[] args) throws Exception {
    ActorSystem system = ActorSystem.create("demo1");
    ActorFlowMaterializer materializer = ActorFlowMaterializer.create(system);

    // Create a source with some integer and print them
    IntStream stream = IntStream.range(1, 11);
    Source<Integer, BoxedUnit> source = Source.from(() -> stream.iterator());
    Integer zero = 0;
    Function2<Integer, Integer, Integer> f = (x, y) -> x + y;
    Sink<Integer, Future<Integer>> foldingSink = Sink.fold(zero, f);
    Future<Integer> foldingResult = source.runWith(foldingSink, materializer);

    System.out.println(Await.result(foldingResult, Duration.Inf()));
  }
}
