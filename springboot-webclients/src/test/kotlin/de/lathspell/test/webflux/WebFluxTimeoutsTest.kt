package de.lathspell.test.webflux

import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpHeaders
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.netty.Connection
import reactor.netty.http.client.HttpClient

/** Advanced configuration of the WebClient e.g. for timeouts. */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WebFluxTimeoutsTest(@LocalServerPort port: Int) {

    private val baseUrl = "http://localhost:$port"
    private val connectTimeoutSecs = 1000
    private val readTimeoutSecs = 2000
    private val writeTimeoutSecs = 3000

    @Test
    fun `webclient with timeout`() {
        val client = myWebClient()
        val actual = client.get().uri("/hello-world/txt").retrieve()
            .bodyToMono<String>()
            .block()!!
        Assertions.assertThat(actual).isEqualTo("Hello World")
    }

    private fun myWebClient(): WebClient = WebClient.builder()
        .clientConnector(ReactorClientHttpConnector(myHttpClient()))
        .defaultHeader(HttpHeaders.USER_AGENT, "Foo")
        .baseUrl(baseUrl)
        .build()

    private fun myHttpClient(): HttpClient = HttpClient.create()
        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeoutSecs * 1000)
        .doOnConnected { c: Connection ->
            c.addHandlerLast(ReadTimeoutHandler(readTimeoutSecs))
            c.addHandlerLast(WriteTimeoutHandler(writeTimeoutSecs))
        }
}
