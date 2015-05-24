package demo2;

import java.util.stream.IntStream;

import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import scala.runtime.BoxedUnit;
import akka.actor.ActorSystem;
import akka.stream.ActorFlowMaterializer;
import akka.stream.FlowShape;
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

public class GraphEx {
  public static void main(String[] args) throws Exception {
    ActorSystem system = ActorSystem.create("demo2");
    ActorFlowMaterializer materializer = ActorFlowMaterializer.create(system);

    // create a stream for the following graph
    //                      f2
    // in -> f1 -> bcast /     \ merge -> f3 -> out
    //                   \  f4 /
    Sink<Integer, Future<BoxedUnit>> aSink = Sink.<Integer> foreach(x -> System.out.println(x));
    RunnableFlow<Future<BoxedUnit>> runnable = FlowGraph.factory().closed(aSink, (builder, out) -> {
      IntStream stream = IntStream.range(1, 4);
      Outlet<Integer> in = builder.source(Source.from(() -> stream.iterator()));
      FlowShape<Integer, Integer> f1 = builder.graph(Flow.<Integer> create().map(x -> x + 10));
      FlowShape<Integer, Integer> f2 = builder.graph(Flow.<Integer> create().map(x -> x + 10));
      FlowShape<Integer, Integer> f3 = builder.graph(Flow.<Integer> create().map(x -> x + 10));
      FlowShape<Integer, Integer> f4 = builder.graph(Flow.<Integer> create().map(x -> x + 10));
      UniformFanOutShape<Integer, Integer> bcast = builder.graph(Broadcast.create(2));
      UniformFanInShape<Integer, Integer> merge = builder.graph(Merge.create(2));

      builder.edge(in, f1.inlet());
      builder.edge(f1.outlet(), bcast.in());
      builder.edge(bcast.out(0), f2.inlet());
      builder.edge(bcast.out(1), f4.inlet());
      builder.edge(f2.outlet(), merge.in(0));
      builder.edge(f4.outlet(), merge.in(1));
      builder.edge(merge.out(), f3.inlet());
      builder.edge(f3.outlet(), out.inlet());
    });
    Future<BoxedUnit> res = runnable.run(materializer);
    Await.result(res, Duration.Inf());
  }
}
