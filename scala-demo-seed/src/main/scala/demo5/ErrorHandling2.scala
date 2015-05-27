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

  // Create a materializer with a supervision strategy that Resume if an ArithmeticException occurs.
  // The superision strategy is applied to the whole stream.

}