package demo2;

import java.util.stream.IntStream;

import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.ClosedShape;
import akka.stream.FlowShape;
import akka.stream.Outlet;
import akka.stream.UniformFanInShape;
import akka.stream.UniformFanOutShape;
import akka.stream.javadsl.Broadcast;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.FlowGraph;
import akka.stream.javadsl.Merge;
import akka.stream.javadsl.RunnableGraph;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import scala.runtime.BoxedUnit;

public class GraphEx {
  public static void main(String[] args) throws Exception {
    ActorSystem system = ActorSystem.create("demo2");
    ActorMaterializer materializer = ActorMaterializer.create(system);

    // create a stream for the following graph
    //                      f2
    // in -> f1 -> bcast /     \ merge -> f3 -> out
    //                   \  f4 /
    Sink<Integer, Future<BoxedUnit>> aSink = Sink.<Integer> foreach(x -> System.out.println(x));
    RunnableGraph<Future<BoxedUnit>> runnable = RunnableGraph.<Future<BoxedUnit>>fromGraph(FlowGraph.create(aSink, (builder, out) -> {
      IntStream stream = IntStream.range(1, 4);
      Outlet<Integer> in = builder.add(Source.from(() -> stream.iterator())).outlet();
      FlowShape<Integer, Integer> f1 = builder.add(Flow.<Integer> create().map(x -> x + 10));
      FlowShape<Integer, Integer> f2 = builder.add(Flow.<Integer> create().map(x -> x + 10));
      FlowShape<Integer, Integer> f3 = builder.add(Flow.<Integer> create().map(x -> x + 10));
      FlowShape<Integer, Integer> f4 = builder.add(Flow.<Integer> create().map(x -> x + 10));
      UniformFanOutShape<Integer, Integer> bcast = builder.add(Broadcast.create(2));
      UniformFanInShape<Integer, Integer> merge = builder.add(Merge.create(2));

      builder.from(in).via(f1).viaFanOut(bcast).via(f2).viaFanIn(merge).via(f3).to(out);
      builder.from(bcast).via(f4).toFanIn(merge);
      return ClosedShape.getInstance();
    }));
    Future<BoxedUnit> res = runnable.run(materializer);
    Await.result(res, Duration.Inf());
  }
}
