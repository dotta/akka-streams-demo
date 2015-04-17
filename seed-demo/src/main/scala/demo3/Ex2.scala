package demo3

import akka.actor.ActorSystem
import akka.stream.ActorFlowMaterializer
import akka.stream._
import akka.stream.scaladsl._
import scala.concurrent.duration._
import akka.stream.scaladsl.FlowGraph.Implicits._
import FlowGraph.Implicits._
import scala.concurrent.Future
import scala.concurrent.Await

object Ex2 extends App {
  implicit val system = ActorSystem("demo3")
  implicit val materializer = ActorFlowMaterializer()
  import system.dispatcher

  // Create a new fan-in junction that takes 3 Int inputs and outputs the max

}