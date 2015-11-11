package demo4;

import java.util.stream.IntStream;

import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.ClosedShape;
import akka.stream.FlowShape;
import akka.stream.Outlet;
import akka.stream.SinkShape;
import akka.stream.UniformFanInShape;
import akka.stream.UniformFanOutShape;
import akka.stream.javadsl.Broadcast;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.FlowGraph;
import akka.stream.javadsl.Merge;
import akka.stream.javadsl.RunnableGraph;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import scala.runtime.BoxedUnit;

public class Ex2 {
  public static void main(String[] args) throws Exception {
    ActorSystem system = ActorSystem.create("demo2");
    ActorMaterializer materializer = ActorMaterializer.create(system);

    // Let's create a cycle
    //
    // source → merge → printer → bcast → sink.ignore
    //             ↑                ↓
    //             ←←←←←←←←←
    RunnableGraph<BoxedUnit> runnable = RunnableGraph.fromGraph(FlowGraph.create(b -> {
      IntStream numbers = IntStream.iterate(1, x -> x + 1);
      Outlet<Integer> source = b.add(Source.from(() -> numbers.iterator())).outlet();
      UniformFanInShape<Integer, Integer> merge = b.add(Merge.create(2));
      UniformFanOutShape<Integer, Integer> bcast = b.add(Broadcast.create(2));
      FlowShape<Integer, Integer> printer = b.add(Flow.of(Integer.class).map(x -> {
        System.out.println(x);
        return x;
      }));
      SinkShape<Integer> sink = b.add(Sink.ignore());

      b.from(source).viaFanIn(merge).via(printer).viaFanOut(bcast).to(sink);
      b.to(merge).fromFanOut(bcast);
      return ClosedShape.getInstance();
    }));

    runnable.run(materializer);
  }
}
