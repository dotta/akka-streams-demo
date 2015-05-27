package demo4;

import java.util.stream.IntStream;

import scala.runtime.BoxedUnit;
import akka.actor.ActorSystem;
import akka.stream.ActorFlowMaterializer;
import akka.stream.FlowShape;
import akka.stream.Inlet;
import akka.stream.Outlet;
import akka.stream.UniformFanInShape;
import akka.stream.UniformFanOutShape;
import akka.stream.javadsl.Broadcast;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.FlowGraph;
import akka.stream.javadsl.Merge;
import akka.stream.javadsl.RunnableFlow;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;

public class Ex2 {
  public static void main(String[] args) throws Exception {
    ActorSystem system = ActorSystem.create("demo2");
    ActorFlowMaterializer materializer = ActorFlowMaterializer.create(system);

    // Let's create a cycle
    //
    // source → merge → printer → bcast → sink.ignore
    //             ↑                ↓
    //             ←←←←←←←←←
    RunnableFlow<BoxedUnit> runnable = FlowGraph.factory().closed(b -> {
      IntStream numbers = IntStream.iterate(1, x -> x + 1);
      Outlet<Integer> source = b.source(Source.from(() -> numbers.iterator()));
      UniformFanInShape<Integer, Integer> merge = b.graph(Merge.create(2));
      UniformFanOutShape<Integer, Integer> bcast = b.graph(Broadcast.create(2));
      FlowShape<Integer, Integer> printer = b.graph(Flow.<Integer> create().map(x -> {
        System.out.println(x);
        return x;
      }));
      Inlet<Integer> sink = b.sink(Sink.<Integer> ignore());

      b.from(source).via(merge).via(printer).via(bcast).to(sink);
      b.from(bcast).to(merge);
    });

    runnable.run(materializer);
  }
}
