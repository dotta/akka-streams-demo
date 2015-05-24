package demo5

import akka.actor.ActorSystem
import akka.stream.ActorFlowMaterializer
import akka.stream._
import akka.stream.scaladsl._
import scala.concurrent.duration._
import akka.stream.scaladsl.FlowGraph.Implicits._
import scala.concurrent.Await

object ErrorHandling2 extends App {

  implicit val system = ActorSystem("demo5")
  import system.dispatcher

  // Create a materializer with a supervision strategy that Resume if and ArithmeticException occurs

  // stream that will complete with a failure because of a division by zero.
  // (this should be similar to what you have implemented in ErrorHandling1.scala)

  //  val source = Source(0 to 5).map(100 / _)
  //  val result = source.runWith(Sink.fold(0)(_ + _))
  //
  //  val res = Await.result(result, Duration.Inf)
  //  println(res) // this will print 228

}