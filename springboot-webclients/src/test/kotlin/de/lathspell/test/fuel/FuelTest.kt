package de.lathspell.test.fuel

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.fuel.core.isSuccessful
import com.github.kittinunf.fuel.coroutines.awaitString
import com.github.kittinunf.fuel.coroutines.awaitStringResponseResult
import com.github.kittinunf.fuel.jackson.responseObject
import com.github.kittinunf.result.getOrElse
import de.lathspell.test.rest.model.Greeting
import de.lathspell.test.restclient.RestClientTest
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.server.LocalServerPort

/**
 * Fuel HTTP Client.
 */
@SpringBootTest(webEnvironment = RANDOM_PORT)
class FuelTest(@LocalServerPort port: Int) {

    private val specialOm = ObjectMapper().registerKotlinModule().registerModule(JavaTimeModule())

    private val baseUri = "http://localhost:$port/hello-world"

    private val log = LoggerFactory.getLogger(RestClientTest::class.java)

    @Test
    fun `get text`() {
        val (request, response, result) = Fuel.get("$baseUri/txt")
            .header("Accept", "text/plain")
            .responseString()

        check(response.isSuccessful) { "Request " + request.method + " " + request.url + ": " + response.statusCode + ": " + response.data.toString() }

        assertThat(result.get()).isEqualTo("Hello World")
    }

    @Test
    fun `get with query parameters`() {
        val (request, response, result) = Fuel.get("$baseUri/txt", parameters = listOf("lang" to "de"))
            .responseString()

        check(response.isSuccessful) { "Request " + request.method + " " + request.url + ": " + response.statusCode + ": " + response.data.toString() }

        assertThat(result.get()).isEqualTo("Hallo Welt")
    }

    @Test
    fun `get json object`() {
        val (request, response, result) = Fuel.get("$baseUri/json").responseObject<Greeting>()

        check(response.isSuccessful) { "Request " + request.method + " " + request.url + ": " + response.statusCode + ": " + response.data.toString() }

        assertThat(result.get()).isEqualTo(Greeting(first = "Hello", second = "World"))
    }

    @Test
    fun `get with fold() error handling`() {
        val ex = assertThrows<Exception> {

            val (request, response, result) = Fuel.get("$baseUri/does-not-exist").responseString()
            result.fold(
                { println("success: $it") },
                { throw Exception("Error ${request.method} ${request.url}: ${response.statusCode} ${response.responseMessage}") }
            )

        }
        assertThat(ex.message).endsWith("/hello-world/does-not-exist: 404 Not Found")
    }

    @Test
    fun `get with getOrElse handling`() {
        val ex = assertThrows<Exception> {

            val (_, _, result) = Fuel.get("$baseUri/does-not-exist").responseString()
            val greeting = result.getOrElse {
                throw Exception("Error: ${it.response}")
            }
            println(greeting)

        }
        assertThat(ex.message).contains("<-- 404")
    }

    @Test
    fun `get json object with complicated request`() {
        val manager = FuelManager()
            .addRequestInterceptor(myLoggingRequestInterceptor())
            .addResponseInterceptor(myLoggingResponseInterceptor())

        val (request, response, result) = manager.get("$baseUri/json")
            .timeout(2000)
            .timeoutRead(1000)
            .authentication().basic("Max", "secret")
            .responseObject<Greeting>(specialOm)

        check(response.isSuccessful) { "Request " + request.method + " " + request.url + ": " + response.statusCode + ": " + response.data.toString() }

        assertThat(result.get()).isEqualTo(Greeting(first = "Hello", second = "World"))
    }

    @Test
    fun `get async using coroutines`() {
        var result: String
        runBlocking {
            result = Fuel.get("$baseUri/txt").awaitString()
        }
        assertThat(result).isEqualTo("Hello World")
    }

    @Test
    fun `get async with coroutines and fold`() {
        var greeting: String = ""
        runBlocking {
            val (request, response, result) = Fuel.get("$baseUri/txt").awaitStringResponseResult()

            result.fold(
                { greeting = it },
                { throw Exception("Error ${request.method} ${request.url}: ${response.statusCode} ${response.responseMessage}") }
            )
        }
        assertThat(greeting).isEqualTo("Hello World")
    }

    private fun myLoggingRequestInterceptor() =
        { next: (Request) -> Request ->
            { req: Request ->
                log.debug("Request: $req")
                next(req)
            }
        }

    private fun myLoggingResponseInterceptor() =
        { next: (Request, Response) -> Response ->
            { req: Request, resp: Response ->
                log.debug("Response: $resp")
                log.debug("Body: " + String(resp.body().toByteArray()))
                next(req, resp)
            }
        }

}
