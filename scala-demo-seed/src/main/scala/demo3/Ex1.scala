package demo3

import akka.actor.ActorSystem
import akka.stream.ActorFlowMaterializer
import akka.stream._
import akka.stream.scaladsl._
import scala.concurrent.duration._
import akka.stream.scaladsl.FlowGraph.Implicits._

object Ex1 extends App {
  implicit val system = ActorSystem("demo3")
  implicit val materializer = ActorFlowMaterializer()
  import system.dispatcher

  // Create a stream composing two sources

  val runnable = FlowGraph.closed() { implicit b =>
    val s1 = Source(1 to 3)
    val s2 = Source('a' to 'c')

    val zip = b.add(Zip[Int,Char]())

    s1 ~> zip.in0
    s2 ~> zip.in1
    zip.out ~> Sink.foreach(println)
  }

  runnable.run()
}