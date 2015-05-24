package demo4;

import java.util.stream.IntStream;

import scala.runtime.BoxedUnit;
import akka.actor.ActorSystem;
import akka.stream.ActorFlowMaterializer;
import akka.stream.javadsl.Source;

public class Ex1 {

  public static void main(String[] args) {
    ActorSystem system = ActorSystem.create("demo4");
    ActorFlowMaterializer materializer = ActorFlowMaterializer.create(system);

    // Create a slow stream and show CPUs usage
    IntStream numbers = IntStream.iterate(1, x -> x + 1);
    Source<Integer, BoxedUnit> source = Source.from(() -> numbers.iterator());
    source.map(e -> { Thread.sleep(1000); return e; }).runForeach(x -> System.out.println(x), materializer);
  }

}
