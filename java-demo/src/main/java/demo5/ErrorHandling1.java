package demo5;

import java.util.stream.IntStream;

import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

public class ErrorHandling1 {
  public static void main(String[] args) throws Exception {
    ActorSystem system = ActorSystem.create("demo5");
    ActorMaterializer materializer = ActorMaterializer.create(system);

    // // Here is a stream that will complete with a failure because of a division by zero
    IntStream stream = IntStream.range(0, 5);
    Source<Integer, ?> source = Source.from(() -> stream.iterator()).map(x -> 100 / x);
    Future<Integer> result = source.runWith(Sink.fold(0, (x, y) -> x + y), materializer);
    Integer res = Await.result(result, Duration.Inf());

    System.out.println(res); // this will print an ArithmeticException
  }
}
