package de.lathspell.test.okhttp

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import de.lathspell.test.rest.model.Greeting
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.server.LocalServerPort

@SpringBootTest(webEnvironment = RANDOM_PORT)
class OkHttpTest(@LocalServerPort port: Int) {

    private val baseUri = "http://localhost:$port/hello-world"
    private val client: OkHttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor(LoggingInterceptor())
        .build()

    private val om = ObjectMapper().registerKotlinModule().registerModule(JavaTimeModule())

    @Test
    fun `get text with response entity`() {
        val request = Request.Builder()
            .get().url("$baseUri/txt")
            .header("User-Agent", "OkHttp Example")
            .build()

        val response = client.newCall(request).execute()

        assertThat(response.code).isEqualTo(200)
        assertThat(response.isSuccessful).isTrue
        assertThat(response.body!!.string()).isEqualTo("Hello World")
    }

    @Test
    fun `get json object`() {
        val request = Request.Builder()
            .get().url("$baseUri/json")
            .build()

        val response = client.newCall(request).execute()
        assertThat(response.isSuccessful).isTrue

        val greeting = om.readValue<Greeting>(response.body!!.string())
        assertThat(greeting).isEqualTo(Greeting(first = "Hello", second = "World"))
    }

    private class LoggingInterceptor : Interceptor {

        private val log = LoggerFactory.getLogger(LoggingInterceptor::class.java)

        override fun intercept(chain: Interceptor.Chain): Response {
            val request: Request = chain.request()
            val t1 = System.nanoTime()
            log.info("Sending request ${request.url} on ${chain.connection()}\n${request.headers}")

            val response: Response = chain.proceed(request)
            val t2 = System.nanoTime()
            log.info("Received response for ${response.request.url} in " + ((t2 - t1) / 1e6) + "\n${response.headers}")
            return response
        }
    }
}
