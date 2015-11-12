package demo4;

import java.util.stream.IntStream;

import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Source;

public class Ex1 {

  public static void main(String[] args) {
    ActorSystem system = ActorSystem.create("demo4");
    ActorMaterializer materializer = ActorMaterializer.create(system);

    // Create a slow stream and show CPUs usage
    IntStream numbers = IntStream.iterate(1, x -> x + 1);
    Source<Integer, ?> source = Source.from(() -> numbers.iterator());
    source.map(e -> { Thread.sleep(1000); return e; }).runForeach(x -> System.out.println(x), materializer);
  }

}
