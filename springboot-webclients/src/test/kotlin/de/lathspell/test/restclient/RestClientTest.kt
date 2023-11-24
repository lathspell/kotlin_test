package de.lathspell.test.restclient

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import de.lathspell.test.rest.model.Greeting
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestClient
import org.springframework.web.client.body
import java.time.Instant

/**
 * SpringBoot 3.2 RestClient HTTP Client.
 */
@SpringBootTest(webEnvironment = RANDOM_PORT)
class RestClientTest(@LocalServerPort port: Int) {

    private val specialOm = ObjectMapper().registerKotlinModule().registerModule(JavaTimeModule())

    private val log = LoggerFactory.getLogger(RestClientTest::class.java)

    private val baseUrl = "http://localhost:$port/hello-world"

    private val restClient = RestClient.builder()
        .baseUrl(baseUrl)
        .defaultHeaders { it.setBearerAuth("ey...") }
        .build()

    @Test
    fun `get text`() {
        val result = restClient.get().uri("/txt").header("Accept", "text/plain").retrieve().body<String>()
        assertThat(result).isEqualTo("Hello World")
    }

    @Test
    fun `get with query parameters`() {
        val result = restClient.get().uri("/txt?lang=de").header("Accept", "text/plain").retrieve().body<String>()
        assertThat(result).isEqualTo("Hallo Welt")
    }

    @Test
    fun `get json object`() {
        val result = restClient.get().uri("/json").retrieve().body<Greeting>()
        assertThat(result).isEqualTo(Greeting(first = "Hello", second = "World"))
    }

    @Test
    fun `get with fold() error handling`() {
        val ex = assertThrows<HttpClientErrorException> {
            val result = restClient.get().uri("/does-not-exist").retrieve().body<String>()
            println("success: $result")
        }
        assertThat(ex.statusCode.value()).isEqualTo(404)
        assertThat(ex.responseBodyAsString).contains(""""path":"/hello-world/does-not-exist",""")
        assertThat(ex.message).startsWith("404 Not Found")
    }

    @Test
    fun `get json object with complicated request`() {
        val client = RestClient.builder()
            .requestFactory(SimpleClientHttpRequestFactory().apply {
                setConnectTimeout(1000)
                setReadTimeout(2000)
            })
            .requestInterceptor(myLoggingInterceptor())
            .defaultHeaders {
                it.setBasicAuth("Max", "secret")
            }
            .messageConverters {
                it.add(MappingJackson2HttpMessageConverter(specialOm))
            }
            .baseUrl(baseUrl)
            .build()

        val result = client.get().uri("/json").retrieve().body<Greeting>()

        assertThat(result).isEqualTo(Greeting(first = "Hello", second = "World"))
    }

    private fun myLoggingInterceptor() = ClientHttpRequestInterceptor { request, body, execution ->
        val t0 = Instant.now().toEpochMilli()
        log.debug("Request: {} {}", request.method, request.uri)

        val response = execution.execute(request, body)

        val duration = Instant.now().toEpochMilli() - t0
        log.debug("Response: {} {} => {} ({} ms)", request.method, request.uri, response.statusCode, duration)

        response
    }
}
