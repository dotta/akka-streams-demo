package demo3;

import java.util.Arrays;
import java.util.stream.IntStream;

import akka.actor.ActorSystem;
import akka.japi.Pair;
import akka.stream.ActorMaterializer;
import akka.stream.FanInShape2;
import akka.stream.SourceShape;
import akka.stream.javadsl.FlowGraph;
import akka.stream.javadsl.Source;
import akka.stream.javadsl.Zip;
import scala.runtime.BoxedUnit;

public class Ex1 {
  public static void main(String[] args) throws Exception {
    ActorSystem system = ActorSystem.create("demo3");
    ActorMaterializer materializer = ActorMaterializer.create(system);

    Source<Integer, BoxedUnit> numbers = Source.from(() -> IntStream.range(1, 4).iterator());
    Source<Character, BoxedUnit> chars = Source.from(Arrays.asList('a', 'b', 'c'));

    Source<Pair<Integer, Character>, BoxedUnit> composite = Source.fromGraph(FlowGraph.create(b -> {
      FanInShape2<Integer, Character, Pair<Integer, Character>> zip = b.add(Zip.<Integer, Character> create());

      b.from(b.add(numbers)).toInlet(zip.in0());
      b.from(b.add(chars)).toInlet(zip.in1());
      return SourceShape.of(zip.out());
    }));

    composite.runForeach(x -> System.out.println(x), materializer);
  }
}
