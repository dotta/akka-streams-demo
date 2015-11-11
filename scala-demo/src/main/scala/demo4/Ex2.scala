package demo4

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream._
import akka.stream.scaladsl._
import scala.concurrent.duration._
import akka.stream.scaladsl.FlowGraph.Implicits._

object Ex2 extends App {
  implicit val system = ActorSystem("demo4")
  implicit val materializer = ActorMaterializer()
  import system.dispatcher

  // Let's create a cycle
  //
  // source → merge → printer → bcast → sink.ignore
  //             ↑                ↓
  //             ←←←←←←←←←
  val source = Source(() => Iterator.from(1))
  val g = RunnableGraph.fromGraph(
    FlowGraph.create() { implicit b =>
      val merge = b.add(Merge[Int](2))
      val bcast = b.add(Broadcast[Int](2))
      val printer = Flow[Int].map {e => println(e); e}
      source ~> merge ~> printer ~> bcast ~> Sink.ignore
      merge      <~   bcast

      ClosedShape
    }
  )
  g.run()

  // Show that it deadlocks
  // Then show how to fix it with an unfair PreferredMerge junction
  // Then show how to properly fix it by adding a buffering stage
}