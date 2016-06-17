package org.zalando.nakadi.client.scala

import org.zalando.nakadi.client.utils.ClientBuilder
import java.util.function.Supplier

object ClientFactory {
  import sys.process._
  import scala.language.postfixOps
  def host(): String = "nakadi.test.fernando.io"
  def localHost(): String = "localhost"
  def localPort(): Integer = 8080
  def OAuth2Token(): Option[() => String] = Option(() => "1c1d7814-c31e-40b8-bbca-f340c6909b63")
  def port(): Integer = 443
  def getJavaClient() =
    builder().buildJavaClient();

  def getScalaClient() = builder().build()

  private def builder() = {
//	  useTest()
//    useStaging()
    useLocal()
  }
  private def useLocal() = {
    new ClientBuilder() //
      .withHost("localhost") //
      .withPort(8080) //
      .withSecuredConnection(false) // s
      .withVerifiedSslCertificate(false) // s
  }
  private def useTest() = {
    ClientBuilder()
      .withHost("nakadi-sandbox.aruha-test.zalan.do")
      .withPort(443)
      .withSecuredConnection(true) //s
      .withVerifiedSslCertificate(false) //s
      .withTokenProvider(ClientFactory.OAuth2Token())
  }
  private def useStaging() = {
	  ClientBuilder()
	  .withHost("nakadi-staging.aruha-test.zalan.do")
	  .withPort(443)
	  .withSecuredConnection(true) //s
	  .withVerifiedSslCertificate(false) //s
	  .withTokenProvider(ClientFactory.OAuth2Token())
  }
}