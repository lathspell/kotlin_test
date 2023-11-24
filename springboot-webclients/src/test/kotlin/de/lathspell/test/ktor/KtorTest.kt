package de.lathspell.test.ktor

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import de.lathspell.test.rest.model.Greeting
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.features.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.features.json.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.ContentType.*
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.server.LocalServerPort

@SpringBootTest(webEnvironment = RANDOM_PORT)
class KtorTest(@LocalServerPort port: Int) {

    private val specialOm = ObjectMapper().registerKotlinModule().registerModule(JavaTimeModule())

    private val baseUri = "http://localhost:$port/hello-world"
    private val client = HttpClient(Apache) {
        engine {
        }
        install(HttpTimeout) {
            connectTimeoutMillis = 10_000
            requestTimeoutMillis = 10_000
            socketTimeoutMillis = 10_000
        }
        install(Logging) {
            level = LogLevel.HEADERS
        }
        install(Auth) {
            basic {
                credentials { BasicAuthCredentials("username", "password") }
            }
        }
        install(JsonFeature) {
            serializer = JacksonSerializer(jackson = specialOm) {
                enable(SerializationFeature.INDENT_OUTPUT)
            }
        }
        defaultRequest {
            header("X-Api-Key", "secret")
        }
        // or simply "expectSuccess = false"
        HttpResponseValidator {
            validateResponse { response ->
                if (response.status.value >= 300) {
                    throw RuntimeException("Error: " + response.status.value)
                }
            }
        }
    }

    @Test
    fun `get text with response entity`() {
        runBlocking {
            val response = client.get<String>("$baseUri/txt") {
                accept(Text.Plain)
            }
            Assertions.assertThat(response).isEqualTo("Hello World")
        }
    }

    @Test
    fun `get json object`() {
        runBlocking {
            val response = client.get<Greeting>("$baseUri/json")
            Assertions.assertThat(response).isEqualTo(Greeting(first = "Hello", second = "World"))
        }
    }
}
