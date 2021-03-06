package org.zalando.nakadi.client.scala
import org.scalatest.Matchers
import org.scalatest.WordSpec
import org.zalando.nakadi.client.scala.model.Cursor
import org.zalando.nakadi.client.scala.model.PartitionStrategyType
import org.zalando.nakadi.client.scala.test.factory.EventIntegrationHelper
import org.zalando.nakadi.client.scala.test.factory.events.MySimpleEvent
import org.zalando.nakadi.client.scala.test.factory.events.SimpleEventListener
import org.zalando.nakadi.client.scala.model.PartitionStrategy
import org.scalatest.BeforeAndAfter
import scala.concurrent.Await
import scala.concurrent.duration.DurationInt
import org.scalatest.BeforeAndAfterAll
import scala.util.Random

class ClientIntegrationTest extends WordSpec with Matchers with BeforeAndAfterAll {

  import org.scalatest.Matchers._
  import ClientFactory._
  import MySimpleEvent._

  val client = ClientFactory.getScalaClient()

  override def afterAll {
    client.stop()
  }

  "create and get an eventType" in {
    val eventGenerator = new DefaultMySimpleEventGenerator() {
      def eventTypeId = s"ClientIntegrationTest-Scala"
    }
    val eventType = eventGenerator.eventType
    //POST
    val creationResult = Await.result(client.createEventType(eventType), 10.seconds)
    creationResult shouldBe None

    //GET
    {
      val eventClientResult = Await.result(client.getEventType(eventType.name), 10.seconds)
      eventClientResult.isRight shouldBe true
      val Right(eventTypeOpt) = eventClientResult
      eventTypeOpt.isDefined shouldBe true
      val Some(eventTypeResult) = eventTypeOpt

      //    Compare EventType
      eventType.category shouldBe eventTypeResult.category
      eventType.dataKeyFields shouldBe List()
      eventType.name shouldBe eventTypeResult.name
      eventType.owningApplication shouldBe eventTypeResult.owningApplication
      eventType.partitionStrategy shouldBe eventTypeResult.partitionStrategy
      eventType.schema shouldBe eventTypeResult.schema
      eventType.statistics shouldBe eventTypeResult.statistics
      eventType.enrichmentStrategies shouldBe eventTypeResult.enrichmentStrategies
      eventType.partitionKeyFields shouldBe eventTypeResult.partitionKeyFields
    }
  }
  "update an existing eventType" in {
    val eventGenerator = new DefaultMySimpleEventGenerator() {
      def eventTypeId = s"ClientIntegrationTest-Scala"
    }
    val eventType = eventGenerator.eventType
    
    //POST
    val creationResult = Await.result(client.createEventType(eventType), 10.seconds)
    creationResult shouldBe None

    //UPDATE
    val changedEventType = eventType.copy(owningApplication = "Nakadi-klients(integration-test-suite)2", enrichmentStrategies = List())
    eventType.owningApplication should not be changedEventType.owningApplication
    
    val updateResult = Await.result(client.updateEventType(eventType.name, changedEventType), 10.seconds)
    
    updateResult.isEmpty shouldBe true //Error should be empty
    val eventClientResult = Await.result(client.getEventType(eventType.name), 10.seconds)
    eventClientResult.isRight shouldBe true
    val Right(eventTypeOpt) = eventClientResult
    eventTypeOpt.isDefined shouldBe true
    val Some(eventTypeResult) = eventTypeOpt

    //Check that it is not equal to original
    eventType.owningApplication should not be eventTypeResult.owningApplication

    //The changed one
    changedEventType.name shouldBe eventTypeResult.name
    changedEventType.category shouldBe eventTypeResult.category
    changedEventType.dataKeyFields shouldBe List()
    changedEventType.name shouldBe eventTypeResult.name
    changedEventType.owningApplication shouldBe eventTypeResult.owningApplication
    changedEventType.partitionStrategy shouldBe eventTypeResult.partitionStrategy
    changedEventType.schema shouldBe eventTypeResult.schema
    changedEventType.statistics shouldBe eventTypeResult.statistics
    changedEventType.enrichmentStrategies shouldBe eventTypeResult.enrichmentStrategies
    changedEventType.partitionKeyFields shouldBe eventTypeResult.partitionKeyFields

  }

  "POST & GET & DELETE /event-types/{name}" in {
    val eventGenerator = new DefaultMySimpleEventGenerator() {
      def eventTypeId = s"ClientIntegrationTest-Scala"
    }
    val eventType = eventGenerator.eventType
    //POST
    val creationResult = Await.result(client.createEventType(eventType), 10.seconds)
    creationResult shouldBe None

    //GET
    {
      val eventClientResult = Await.result(client.getEventType(eventType.name), 10.seconds)
      eventClientResult.isRight shouldBe true
      eventClientResult.right.get.isDefined shouldBe true
    } 
    //DELETE
    val deletedResult = Await.result(client.deleteEventType(eventType.name), 10.seconds)
    deletedResult.isEmpty shouldBe true

    //GET
    val eventClientResult = Await.result(client.getEventType(eventType.name), 10.seconds)
    eventClientResult.isRight shouldBe true

    //Result is empty(404)
    eventClientResult.right.get.isDefined shouldBe false

  }

  "GET /metrics" in {
    val result = Await.result(client.getMetrics(), 10.seconds)
    result.isRight shouldBe true
    val Right(metricsOpt) = result
    metricsOpt.isDefined shouldBe true
    val Some(metrics) = metricsOpt
    println(metrics)
    metrics.version shouldNot be(null)
    metrics.gauges.size should be > 0
  }

  "GET /event-types" in {
    val result = Await.result(client.getEventTypes(), 10.seconds)
    result.isRight shouldBe true
    val Right(eventTypesOpt) = result
    eventTypesOpt.isDefined shouldBe true
    val Some(eventTypes) = eventTypesOpt
    eventTypes.size should (equal(0) or (be > 0))
  }

  "GET /registry/partitions-strategies" in {
    val result = Await.result(client.getPartitioningStrategies(), 10.seconds)
    result.isRight shouldBe true
  }

  "GET /registry/enrichment-strategies" in {
    val result = Await.result(client.getEnrichmentStrategies(), 10.seconds)
    result.isRight shouldBe true
    val Right(strategies) = result
    strategies.isDefined shouldBe true
  }

}

