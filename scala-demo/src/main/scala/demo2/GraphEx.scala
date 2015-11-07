package demo2

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream._
import akka.stream.scaladsl._

import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.duration._

// Important for using the FLowGraph DSL!
import FlowGraph.Implicits._

object GraphEx extends App {
  implicit val system = ActorSystem("demo2")
  implicit val materializer = ActorMaterializer()
  import system.dispatcher

  // create a stream for the following graph
  //                      f2
  // in -> f1 -> bcast /     \ merge -> f3 -> out
  //                   \  f4 /
  val sink = Sink.foreach(println)
  val g = FlowGraph.closed(sink) { implicit b =>
    sink =>
      val in = Source(List(1, 2, 3))
      val bcast = b.add(Broadcast[Int](2))
      val merge = b.add(Merge[Int](2))

      val f1, f2, f3, f4 = Flow[Int].map(_ + 10)

      in ~> f1 ~> bcast ~> f2 ~> merge ~> f3 ~> sink
      bcast ~> f4 ~> merge
  }

  val res = g.run()
  Await.result(res, Duration.Inf)
}