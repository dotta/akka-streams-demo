package demo1

import akka.actor.ActorSystem
import akka.stream.ActorFlowMaterializer
import akka.stream._
import akka.stream.scaladsl._

import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.duration._

object Ex2 extends App {
  implicit val system = ActorSystem("demo1")
  implicit val materializer = ActorFlowMaterializer()
  import system.dispatcher

  // Create a stream with a mapping stage (using a Flow)
  val source = Source(1 to 3)
  val flow = Flow[Int].map(_ + 10).filter(_ % 2 == 0)
  val sink = Sink.foreach(println)
  source.via(flow).runWith(sink)
}