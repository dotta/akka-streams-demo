package demo3;

import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import scala.runtime.BoxedUnit;
import akka.actor.ActorSystem;
import akka.stream.ActorFlowMaterializer;
import akka.stream.FanInShape2;
import akka.stream.Graph;
import akka.stream.Inlet;
import akka.stream.Outlet;
import akka.stream.UniformFanInShape;
import akka.stream.javadsl.FlowGraph;
import akka.stream.javadsl.RunnableFlow;
import akka.stream.javadsl.Source;
import akka.stream.javadsl.ZipWith;
import akka.stream.scaladsl.Sink;

public class Ex2 {
  public static void main(String[] args) throws Exception {
    ActorSystem system = ActorSystem.create("demo3");
    ActorFlowMaterializer materializer = ActorFlowMaterializer.create(system);

    // Create a new fan-in junction that takes 3 Integer inputs and outputs the max
    Graph<UniformFanInShape<Integer, Integer>, BoxedUnit> maxOfThree = FlowGraph.factory().partial(
        b -> {
          FanInShape2<Integer, Integer, Integer> zip1 = b.graph(ZipWith
              .<Integer, Integer, Integer> create((x, y) -> Math.max(x, y)));
          FanInShape2<Integer, Integer, Integer> zip2 = b.graph(ZipWith
              .<Integer, Integer, Integer> create((x, y) -> Math.max(x, y)));

          b.edge(zip1.out(), zip2.in0());

          
          return new UniformFanInShape<Integer, Integer>(zip2.out(), 
              new Inlet[] {zip1.in0(), zip1.in1(), zip2.in1()});
        });
    Sink<Integer, Future<Integer>> sink = Sink.head();

    RunnableFlow<Future<Integer>> runnable = FlowGraph.factory().closed(sink, (b, out) -> {
      Outlet<Integer> s1 = b.source(Source.single(1));
      Outlet<Integer> s2 = b.source(Source.single(2));
      Outlet<Integer> s3 = b.source(Source.single(3));
      UniformFanInShape<Integer, Integer> pm3 = b.graph(maxOfThree);

      b.edge(s1, pm3.in(0));
      b.edge(s2, pm3.in(1));
      b.edge(s3, pm3.in(2));
      b.edge(pm3.out(), out.inlet());
    });

    Future<Integer> max = runnable.run(materializer);
    Integer res = Await.result(max, Duration.Inf());
    System.out.println(res);
  }
}
