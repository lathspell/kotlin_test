package de.lathspell.test.webflux

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.toEntity
import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers
import reactor.kotlin.core.publisher.toFlux

/** Using WebClient for simple non-blocking requests. */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WebFluxNonblockingTest(@LocalServerPort port: Int) {

    private val log = LoggerFactory.getLogger(WebFluxNonblockingTest::class.java)

    private val baseUri = "http://localhost:$port"
    private val client = WebClient.create(baseUri)

    /** Our 10 HTTP requests form are made into a Flux because only then we can use runOn() to have them executed in parallel. */
    @Test
    fun `multiple requests simultaneously`() {
        val num = 10

        log.info("Launching request")
        val responses = Flux.fromIterable(1..num)
            .flatMap { i ->
                log.info("Requesting $i")
                client.get().uri("/slow?i=$i").accept(MediaType.TEXT_PLAIN).retrieve()
                    .toEntity<String>()
            }
            .parallel(10, 10) // prepares for parallelism and enables .runOn()
            .runOn(Schedulers.parallel())
            .toFlux()
            .collectList()
            .block()

        // do something
        log.info("Waiting")

        // collect data (have to block here)
        log.info("Collecting responses")
        Assertions.assertThat(responses).hasSize(num)
        Assertions.assertThat(responses).allMatch { it.body!! == "slow" }
    }
}
