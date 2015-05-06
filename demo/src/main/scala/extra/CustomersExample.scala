// This is a sample code that was shamelessly stolen from
// http://boldradius.com/blog-post/VS0NpTAAADAACs_E/introduction-to-akka-streams
package extra

import akka.actor.ActorSystem
import akka.stream.ActorFlowMaterializer
import akka.stream.scaladsl.{Flow, Sink, Source}

import scala.collection.immutable
import scala.util.Random

object InputCustomer {
  def random():InputCustomer = {
    InputCustomer(s"FirstName${Random.nextInt(1000)} LastName${Random.nextInt(1000)}")
  }
}
case class InputCustomer(name: String)
case class OutputCustomer(firstName: String, lastName: String)

object CustomersExample extends App {
  implicit val actorSystem = ActorSystem()
  import actorSystem.dispatcher
  implicit val flowMaterializer = ActorFlowMaterializer()

  val inputCustomers = Source((1 to 100).map(_ => InputCustomer.random()))

  val normalize = Flow[InputCustomer].mapConcat { input =>
    input.name.split(" ").toList match {
      case firstName::lastName::Nil => immutable.Seq(OutputCustomer(firstName, lastName))
      case _ => immutable.Seq[OutputCustomer]()
    }
  }

  val writeCustomers = Sink.foreach[OutputCustomer] { customer =>
    println(customer)
  }

  inputCustomers.via(normalize).runWith(writeCustomers).andThen {
    case _ =>
      actorSystem.shutdown()
      actorSystem.awaitTermination()
  }
}