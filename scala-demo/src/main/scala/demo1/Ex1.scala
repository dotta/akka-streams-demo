package demo1

import akka.actor.ActorSystem
import akka.stream.ActorFlowMaterializer
import akka.stream._
import akka.stream.scaladsl._

import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.duration._

object Ex1 extends App {
  implicit val system = ActorSystem("demo1")
  implicit val materializer = ActorFlowMaterializer()
  import system.dispatcher

  // Create a source with some integer and print them
  val source = Source(List(1,2,3))
  val sink = Sink.foreach(println)
  val runnable: RunnableFlow[Unit] = source.to(sink)
  runnable.run()
}