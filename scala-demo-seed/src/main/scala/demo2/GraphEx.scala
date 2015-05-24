package demo2

import akka.actor.ActorSystem
import akka.stream.ActorFlowMaterializer
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
  implicit val materializer = ActorFlowMaterializer()
  import system.dispatcher

  // create a stream for the following graph
  //                      f2
  // in -> f1 -> bcast /     \ merge -> f3 -> out
  //                   \  f4 /

  val runnable = FlowGraph.closed() { implicit b =>
    val in = Source(1 to 10)
    val f1, f3 = Flow[Int].map(identity)
    val bcast = b.add(Broadcast[Int](2))
    val f2 = Flow[Int].map( _ + 1)
    val f4 = Flow[Int].map( _ + 100)
    val merge = b.add(Merge[Int](2))
    val out = Sink.foreach(println)

    in ~> f1 ~> bcast ~> f2 ~> merge ~> f3 ~> out
                bcast ~> f4 ~> merge
  }

  runnable.run()
}