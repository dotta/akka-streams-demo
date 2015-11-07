package demo5

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream._
import akka.stream.scaladsl._
import scala.concurrent.duration._
import akka.stream.scaladsl.FlowGraph.Implicits._
import scala.concurrent.Await

object ErrorHandling1 extends App {
  implicit val system = ActorSystem("demo5")
  implicit val materializer = ActorMaterializer()
  import system.dispatcher

  // Create a stream that will complete with a failure because of a division by zero

}