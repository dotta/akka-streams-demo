package demo4

import akka.actor.ActorSystem
import akka.stream.ActorFlowMaterializer
import akka.stream._
import akka.stream.scaladsl._

import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.duration._

object Ex1 extends App {
  implicit val system = ActorSystem("demo4")
  implicit val materializer = ActorFlowMaterializer()
  import system.dispatcher

  // Create a slow stream and show CPUs usage

  val source = Source( () => Iterator.from(1))
  val flow = Flow[Int].map {x => Thread.sleep(1000); x}
  source.via(flow).runForeach(println)
}