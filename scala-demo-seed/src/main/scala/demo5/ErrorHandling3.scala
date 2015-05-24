package demo5

import akka.actor.ActorSystem
import akka.stream.ActorFlowMaterializer
import akka.stream._
import akka.stream.scaladsl._
import scala.concurrent.duration._
import akka.stream.scaladsl.FlowGraph.Implicits._
import scala.concurrent.Await

object ErrorHandling3 extends App {

  implicit val system = ActorSystem("demo5")
  import system.dispatcher

  implicit val mat = ActorFlowMaterializer()

  // This should be the supervision strategy you have implemented in ErrorHandling2.scala
  val decider: Supervision.Decider = {
    case _: ArithmeticException => Supervision.Resume
    case _                      => Supervision.Stop
  }

  // Change the code here so that the division by 0 doesn't stop the stream
  // (don't pass the supervision strategy to the materializer, but create a `section` instead.
  val source = Source(0 to 5).map(100 / _)
  val result = source.runWith(Sink.fold(0)(_ + _))

  val res = Await.result(result, Duration.Inf)
  println(res) // this will print 228

}