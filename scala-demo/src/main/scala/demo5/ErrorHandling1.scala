package demo5

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream._
import akka.stream.scaladsl._
import scala.concurrent.duration._
import akka.stream.scaladsl.FlowGraph.Implicits._
import scala.concurrent.Await

object ErrorHandling1 extends App {

  // Here is a stream that will complete with a failure because of a division by zero
  implicit val system = ActorSystem("demo5")
  import system.dispatcher
  implicit val materializer = ActorMaterializer()

  val source = Source(0 to 5).map(100 / _)
  val result = source.runWith(Sink.fold(0)(_ + _))
  val res = Await.result(result, Duration.Inf)

  println(res) // this will print an ArithmeticException

}