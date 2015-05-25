package demo5;

import java.util.stream.IntStream;

import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import scala.runtime.BoxedUnit;
import akka.actor.ActorSystem;
import akka.japi.function.Function;
import akka.stream.ActorFlowMaterializer;
import akka.stream.ActorFlowMaterializerSettings;
import akka.stream.Supervision;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;

public class ErrorHandling2 {
  public static void main(String[] args) throws Exception {
    ActorSystem system = ActorSystem.create("demo5");

    Function<Throwable, Supervision.Directive> decider = e -> {
      if (e instanceof ArithmeticException)
        return Supervision.resume();
      else
        return Supervision.stop();
    };

    ActorFlowMaterializerSettings settings = ActorFlowMaterializerSettings.create(system).withSupervisionStrategy(
        decider);
    // materializer with a custom supervision strategy
    ActorFlowMaterializer materializer = ActorFlowMaterializer.create(settings, system);

    IntStream stream = IntStream.range(0, 6);
    Source<Integer, BoxedUnit> source = Source.from(() -> stream.iterator()).map(x -> 100 / x);
    Future<Integer> result = source.runWith(Sink.fold(0, (x, y) -> x + y), materializer);
    Integer res = Await.result(result, Duration.Inf());

    System.out.println(res); // this will print 228
  }
}