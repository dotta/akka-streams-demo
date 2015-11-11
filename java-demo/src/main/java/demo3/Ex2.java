package demo3;

import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.ClosedShape;
import akka.stream.FanInShape2;
import akka.stream.Graph;
import akka.stream.Inlet;
import akka.stream.Outlet;
import akka.stream.UniformFanInShape;
import akka.stream.javadsl.FlowGraph;
import akka.stream.javadsl.RunnableGraph;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import akka.stream.javadsl.ZipWith;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import scala.runtime.BoxedUnit;

public class Ex2 {
  public static void main(String[] args) throws Exception {
    ActorSystem system = ActorSystem.create("demo3");
    ActorMaterializer materializer = ActorMaterializer.create(system);

    Graph<FanInShape2<Integer, Integer, Integer>, BoxedUnit> zip =
    		  ZipWith.create((Integer left, Integer right) -> Math.max(left, right));
    // Create a new fan-in junction that takes 3 Integer inputs and outputs the max
    Graph<UniformFanInShape<Integer, Integer>, BoxedUnit> maxOfThree = 
      FlowGraph.create( b -> {
        FanInShape2<Integer, Integer, Integer> zip1 = b.add(zip);
        FanInShape2<Integer, Integer, Integer> zip2 = b.add(zip);

        b.from(zip1.out()).toInlet(zip2.in0());

        // return the shape, which has three inputs and one output
        return new UniformFanInShape<Integer, Integer>(zip2.out(), 
          new Inlet[] {zip1.in0(), zip1.in1(), zip2.in1()});
      });

    Sink<Integer, Future<Integer>> sink = Sink.head();

    RunnableGraph<Future<Integer>> runnable = RunnableGraph.<Future<Integer>>fromGraph(
      FlowGraph.create(sink, (b, out) -> {
        Outlet<Integer> s1 = b.add(Source.single(1)).outlet();
        Outlet<Integer> s2 = b.add(Source.single(2)).outlet();
        Outlet<Integer> s3 = b.add(Source.single(3)).outlet();
        UniformFanInShape<Integer, Integer> pm3 = b.add(maxOfThree);

        b.from(s1).toInlet(pm3.in(0));
        b.from(s2).toInlet(pm3.in(1));
        b.from(s3).toInlet(pm3.in(2));
        b.from(pm3.out()).to(out);
        return ClosedShape.getInstance();
      }
    ));

    Future<Integer> max = runnable.run(materializer);
    Integer res = Await.result(max, Duration.Inf());
    System.out.println(res);
  }
}
