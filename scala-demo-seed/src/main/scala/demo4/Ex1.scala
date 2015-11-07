package demo4

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream._
import akka.stream.scaladsl._

import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.duration._

object Ex1 extends App {
  implicit val system = ActorSystem("demo4")
  implicit val materializer = ActorMaterializer()
  import system.dispatcher

  // Create a slow stream and show CPUs usage

}