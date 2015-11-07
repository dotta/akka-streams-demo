package demo5

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream._
import akka.stream.scaladsl._
import scala.concurrent.duration._
import akka.stream.scaladsl.FlowGraph.Implicits._
import scala.concurrent.Await

object ErrorHandling3 extends App {

  implicit val system = ActorSystem("demo5")
  implicit val mat = ActorMaterializer()
  import system.dispatcher

  // Create a materializer with a supervision strategy that Resume if an ArithmeticException occurs.
  // The supervision strategy is applied only to the relevant junction.

}