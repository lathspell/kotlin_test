package de.lathspell.test.http4k

import org.apache.http.client.config.RequestConfig
import org.apache.http.impl.client.HttpClients
import org.assertj.core.api.Assertions.assertThat
import org.http4k.client.Apache4Client
import org.http4k.client.JavaHttpClient
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Status.Companion.OK
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.server.LocalServerPort

@SpringBootTest(webEnvironment = RANDOM_PORT)
class Http4kTest(@LocalServerPort port: Int) {

    private val baseUri = "http://localhost:$port/hello-world"

    @Test
    fun `simple GET`() {
        val client: HttpHandler = JavaHttpClient()

        val request = Request(GET, "$baseUri/txt")
        val response = client(request)

        assertThat(response.status).isEqualTo(OK)
        assertThat(response.bodyString()).isEqualTo("Hello World")
    }


    @Test
    fun `complex GET`() {
        val myClient = HttpClients.custom()
            .setDefaultRequestConfig(
                RequestConfig.custom()
                    .setConnectTimeout(1000)
                    .setSocketTimeout(2000)
                    .setNormalizeUri(true)
                    .build()
            ).build()

        val client = Apache4Client(client = myClient)

        val request = Request(GET, "$baseUri/txt")
            .header("X-Foo", "Bar")
            .query("lang", "de")
            .body("blah blah")

        val response = client(request)

        assertThat(response.status).isEqualTo(OK)
        assertThat(response.bodyString()).isEqualTo("Hallo Welt")
    }
}
