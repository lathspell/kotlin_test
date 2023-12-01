package de.lathspell.test.rest

import de.lathspell.test.rest.model.Greeting
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType.TEXT_PLAIN_VALUE
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloWorldController {

    private val log = LoggerFactory.getLogger(HelloWorldController::class.java)

    @GetMapping("/hello-world/txt", produces = [TEXT_PLAIN_VALUE])
    fun helloWorldAsText(@RequestParam(required = false) lang: String? = null): String {
        log.info("Handling /hello-world/txt")
        return when (lang) {
            "de" -> "Hallo Welt"
            else -> "Hello World"
        }
    }

    @GetMapping("/hello-world/json")
    fun helloWorldAsJson() = Greeting("Hello", "World")

    @GetMapping("/hello-world/json-list")
    fun helloWorldAsJsonList() = listOf(Greeting("Hello", "World"), Greeting("Hallo", "Welt"))

    @GetMapping("/hello-world/empty-list")
    fun helloWorldAsEmptyList() = emptyList<Greeting>()

    @GetMapping("/slow", produces = [TEXT_PLAIN_VALUE])
    fun slow(@RequestParam("i") i: Int): String {
        log.info("Handling /slow $i")
        Thread.sleep(500)
        return "slow"
    }

    @PatchMapping("/hello-world/patch")
    fun patchGreeting(@RequestBody greeting: Greeting): Greeting = greeting
}
