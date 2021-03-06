package org.zalando.nakadi.client.utils

import org.zalando.nakadi.client.scala.Client
import org.zalando.nakadi.client.java.{Client => JClient}
import org.zalando.nakadi.client.java.{ClientImpl => JClientImpl}
import org.zalando.nakadi.client.scala.ClientImpl
import org.zalando.nakadi.client.scala.Connection
import java.util.function.Supplier
import com.google.common.base.Preconditions._

object ClientBuilder {

  def apply(host: String = null,
            port: Int = DEFAULT_PORT,
            tokenProvider: Option[() => String] = None,
            securedConnection: Boolean = true,
            verifySSlCertificate: Boolean = true) =
    new ClientBuilder(host, port, tokenProvider, securedConnection, verifySSlCertificate)

  private val DEFAULT_PORT = 443
}

class ClientBuilder private (host: String = null, //
                             port: Int, //
                             tokenProvider: Option[() => String] = None, //
                             securedConnection: Boolean = true, //
                             verifySSlCertificate: Boolean = true) {
  def this() =
    this(null, ClientBuilder.DEFAULT_PORT, None, true, true)
  def withHost(host: String): ClientBuilder =
    new ClientBuilder(checkNotNull(host), port, tokenProvider, securedConnection, verifySSlCertificate)

  def withPort(port: Int): ClientBuilder =
    new ClientBuilder(host, port, tokenProvider, securedConnection, verifySSlCertificate)

  def withTokenProvider(tokenProvider: Option[() => String]): ClientBuilder =
    new ClientBuilder(host, port, tokenProvider, securedConnection, verifySSlCertificate)

  def withTokenProvider4Java(tokenProvider: Supplier[String]): ClientBuilder =
    withTokenProvider {
      if (tokenProvider == null) {
        None
      } else {
        Option(() => tokenProvider.get())
      }
    }

  def withSecuredConnection(securedConnection: Boolean = true): ClientBuilder =
    new ClientBuilder(host, port, tokenProvider, checkNotNull(securedConnection), verifySSlCertificate)

  def withVerifiedSslCertificate(verifySSlCertificate: Boolean = true): ClientBuilder =
    new ClientBuilder(host, port, tokenProvider, securedConnection, checkNotNull(verifySSlCertificate))

  def build(): Client =
    Connection.newClient(host, port, tokenProvider, securedConnection, verifySSlCertificate)

  def buildJavaClient(): JClient = {
    val connection =
      Connection.newClientHandler4Java(host, port, tokenProvider, securedConnection, verifySSlCertificate)
    new JClientImpl(connection)
  }

}
