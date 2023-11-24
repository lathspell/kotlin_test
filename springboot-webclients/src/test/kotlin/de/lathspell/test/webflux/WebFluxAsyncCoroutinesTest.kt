package de.lathspell.test.webflux

import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.reactive.function.client.bodyToMono
import java.util.concurrent.Executors

/** Mixing WebClient's WebFlux and Kotlin's Coroutines. */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WebFluxAsyncCoroutinesTest(@LocalServerPort port: Int) {

    private val log = LoggerFactory.getLogger(WebFluxAsyncCoroutinesTest::class.java)
    private val baseUri = "http://localhost:$port"
    private val client = WebClient.create(baseUri)

    @Test
    fun `get text with async`() {
        runBlocking {
            val actual = async {
                client.get().uri("/hello-world/txt").accept(MediaType.TEXT_PLAIN).retrieve()
                    .awaitBody<String>()
            }

            Assertions.assertThat(actual.await()).isEqualTo("Hello World")
        }
    }

    /** Each WebClient call just blocks until it has received an response. But Kotlin calls each of those requests in a different Thread and thus
     * they are executed in parallel. */
    @Test
    fun `get text with async 10x`() {
        val context = Executors.newFixedThreadPool(10).asCoroutineDispatcher() // default is CPU cores - 1
        runBlocking(context = context) {

            val responses = (1..10).map { i ->
                async {
                    log.info("Requesting $i")
                    client.get().uri("/slow?i=$i").retrieve()
                        .bodyToMono<String>()
                        .block()!!
                }
            }

            log.info("Collecting")
            val actual = responses.map { it.await() }

            log.info("Checking")
            Assertions.assertThat(actual).hasSize(10)
            Assertions.assertThat(actual).allMatch { it == "slow" }
        }
    }

    /** While in an async context we can use Kotlin's suspend fun.
     * The await body looks like a blocking function to the webclient but leaves the async scheduling to Kotlin. */
    @Test
    fun `get text with async and suspend fun`() {
        runBlocking {
            launch {
                val actual = txtUsingAwaitBody()
                Assertions.assertThat(actual).isEqualTo("Hello World")
            }
        }
    }

    private suspend fun txtUsingAwaitBody(): String {
        return client.get().uri("/hello-world/txt").accept(MediaType.TEXT_PLAIN).retrieve()
            .awaitBody() // can only be used in a coroutine context like async/suspend
    }
}
