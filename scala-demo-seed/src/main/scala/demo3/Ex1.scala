package demo3

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream._
import akka.stream.scaladsl._
import scala.concurrent.duration._
import akka.stream.scaladsl.FlowGraph.Implicits._

object Ex1 extends App {
  implicit val system = ActorSystem("demo3")
  implicit val materializer = ActorMaterializer()
  import system.dispatcher

  // Create a stream composing two sources

}