package demo5

import akka.actor.ActorSystem
import akka.stream.ActorFlowMaterializer
import akka.stream._
import akka.stream.scaladsl._
import scala.concurrent.duration._
import akka.stream.scaladsl.FlowGraph.Implicits._
import scala.concurrent.Await

object ErrorHandling1 extends App {
  implicit val system = ActorSystem("demo5")
  import system.dispatcher
  implicit val materializer = ActorFlowMaterializer()

  // Create a stream that will complete with a failure because of a division by zero

}