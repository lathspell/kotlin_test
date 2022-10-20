package de.lathspell.test.rest

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloWorldController {

    private val log = LoggerFactory.getLogger(HelloWorldController::class.java)

    @GetMapping("/hello")
    fun hello(@RequestParam name: String): String {
        log.info("Handling /hello with '$name'")
        return "Hello $name"
    }

}
