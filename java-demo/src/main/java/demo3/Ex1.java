package demo3;

import java.util.Arrays;
import java.util.stream.IntStream;

import scala.runtime.BoxedUnit;
import akka.actor.ActorSystem;
import akka.japi.Pair;
import akka.stream.ActorFlowMaterializer;
import akka.stream.FanInShape2;
import akka.stream.Outlet;
import akka.stream.javadsl.Source;
import akka.stream.javadsl.Zip;

public class Ex1 {
  public static void main(String[] args) throws Exception {
    ActorSystem system = ActorSystem.create("demo3");
    ActorFlowMaterializer materializer = ActorFlowMaterializer.create(system);

    Source<Integer, BoxedUnit> numbers = Source.from(() -> IntStream.range(1, 4).iterator());
    Source<Character, BoxedUnit> chars = Source.from(Arrays.asList('a', 'b', 'c'));

    Source<Pair<Integer, Character>, BoxedUnit> composite = Source.factory().create(b -> {
      FanInShape2<Integer, Character, Pair<Integer, Character>> zip = b.graph(Zip.<Integer, Character> create());
      Outlet<Integer> s1 = b.source(numbers);
      Outlet<Character> s2 = b.source(chars);
      b.edge(s1, zip.in0());
      b.edge(s2, zip.in1());
      return zip.out();
    });

    composite.runForeach(x -> System.out.println(x), materializer);
  }
}
