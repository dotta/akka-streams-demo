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

  // Create a materializer with a supervision strategy that Resume if an ArithmeticException occurs.
  // The supervision strategy is applied only to the relevant junction.

}