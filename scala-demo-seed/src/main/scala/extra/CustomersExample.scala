// This is a sample code that was shamelessly stolen from
// http://boldradius.com/blog-post/VS0NpTAAADAACs_E/introduction-to-akka-streams
package extra

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
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
  implicit val actorSystem = ActorSystem("extra")
  implicit val flowMaterializer = ActorMaterializer()
  import actorSystem.dispatcher

  // 1. create the customers to use as input
  // val inputCustomers =

  // 2. create a stage that maps InputCustomer into OutputCustomer
  // val normalize: Flow[InputCustomer, OutputCustomer, Unit] =

  // 3. create a sink that stores the normalized sustomers
  // val writeCustomers

  // 4. create the bluprint and run it, making sure that the actor system is shutted down
}