package demo5

import akka.actor.ActorSystem
import akka.stream.ActorFlowMaterializer
import akka.stream._
import akka.stream.scaladsl._
import scala.concurrent.duration._
import akka.stream.scaladsl.FlowGraph.Implicits._
import scala.concurrent.Await

object ErrorHandling2 extends App {

  implicit val system = ActorSystem("demo5")
  import system.dispatcher

  val decider: Supervision.Decider = {
    case _: ArithmeticException => Supervision.Resume
    case _                      => Supervision.Stop
  }

  // materializer with a custom supervision strategy
  implicit val mat = ActorFlowMaterializer(
    ActorFlowMaterializerSettings(system).withSupervisionStrategy(decider))

  val source = Source(0 to 5).map(100 / _)
  val result = source.runWith(Sink.fold(0)(_ + _))

  val res = Await.result(result, Duration.Inf)
  println(res) // this will print 228

}