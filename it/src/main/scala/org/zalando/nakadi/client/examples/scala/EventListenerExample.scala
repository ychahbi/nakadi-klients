package org.zalando.nakadi.client.examples.scala

import org.zalando.nakadi.client.scala.ClientFactory
import org.zalando.nakadi.client.utils.ClientBuilder
import org.zalando.nakadi.client.scala.Client
import org.zalando.nakadi.client.scala._
import org.zalando.nakadi.client.scala.model._
import EventCreationExample._
import com.fasterxml.jackson.core.`type`.TypeReference
import java.util.concurrent.atomic.AtomicLong
import org.slf4j.LoggerFactory

/**
  * Your listener will have to implement the necessary
  */
class EventCounterListener(val id: String) extends Listener[MeetingsEvent] {
  val log = LoggerFactory.getLogger(this.getClass)
  private var eventCount: AtomicLong = new AtomicLong(0);
  private var callerCount: AtomicLong = new AtomicLong(0);
  private var slept = false
  def onError(sourceUrl: String, error: Option[ClientError]): Unit = {
    println("Error %s %s".format(sourceUrl, error))
  }

  def onReceive(sourceUrl: String,
                cursor: Cursor,
                events: Seq[MeetingsEvent]): Unit = {
    eventCount.addAndGet(events.size.toLong)
//    log.debug("#####################################")
//    log.debug(s"Received " + events.size.toLong)
    log.debug(s"Has a total of $eventCount events")
//    log.debug("#####################################")
//    if (!slept) {
//      Thread.sleep(1000)
//      slept = true
//    }
    log.info(s"Proccessed cursor $cursor")

  }
  def onSubscribed(endpoint: String, cursor: Option[Cursor]): Unit = {
    log.debug("########## onSubscribed ############")
    log.debug("Endpoint " + endpoint)
    log.debug("Cursor " + cursor)
    log.debug("#####################################")

  }
}

object EventListenerExample extends App {

  /**
    * Create our client
    */
  val client: Client = ClientFactory.getScalaClient()

  /**
    * Initialize our Listener
    */
  val listener = new EventCounterListener("Test")

  /**
    * Create the Parameters with the cursor.
    */
  val cursor = Cursor("0", "BEGIN")

  val parameters = new StreamParameters(
      cursor = Some(cursor) //
//    cursor = None //
      ,
      batchLimit = Some(400) //  Maximum number of `Event`s in each chunk (and therefore per partition) of the stream.  
      //        , streamLimit = Some(500) // Maximum number of `Event`s to stream (over all partitions being streamed in this
      //connection).
//            , batchFlushTimeout = Some(10) // Maximum time in seconds to wait for the flushing of each chunk (per partition).
      //        ,streamKeepAliveLimit=Some(4)
      ,
      streamTimeout = Some(0)
  )

  /**
    * Create TypeReference for the JacksonObjectMapper
    */
  implicit def typeRef: TypeReference[EventStreamBatch[MeetingsEvent]] =
    new TypeReference[EventStreamBatch[MeetingsEvent]] {}
  import org.zalando.nakadi.client.scala.model.ScalaJacksonJsonMarshaller._

  //  val eventTypeName = "Event-example-with-0-messages"
  val eventTypeName = "Example-2000"
  val result = client.subscribe(eventTypeName, parameters, listener)

//  Thread.sleep(10000)
//  val listener2 = new EventCounterListener("Test2")
//  client.subscribe(eventTypeName, parameters, listener2)
  //  client.stop()
//  client.unsubscribe(eventTypeName, Some("0"), listener)
  //  client.subscribe(eventTypeName, parameters, listener)

}
