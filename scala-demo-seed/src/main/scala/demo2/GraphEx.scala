package demo2

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream._
import akka.stream.scaladsl._

import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.duration._

// Important for using the FLowGraph DSL!
import FlowGraph.Implicits._

object GraphEx extends App {
  implicit val system = ActorSystem("demo2")
  implicit val materializer = ActorMaterializer()
  import system.dispatcher

  // create a stream for the following graph
  //                      f2
  // in -> f1 -> bcast /     \ merge -> f3 -> out
  //                   \  f4 /

}