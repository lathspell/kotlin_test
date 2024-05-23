package de.lathspell.test

import org.slf4j.LoggerFactory
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

/**
 * Both methods are scheduled in one Thread thus, the `blocker()` method can delay the execution of `everySecond()`.
 * Still, Spring tries to calculate how often the `everySecond()` should have been called and executes it multiple
 * times when the thread is free again:
 *
 * 2024-05-23T21:10:48.036+02:00  WARN 70538 --- [   scheduling-1] de.lathspell.test.service.FooService     : Blocking starts
 * 2024-05-23T21:10:53.040+02:00  WARN 70538 --- [   scheduling-1] de.lathspell.test.service.FooService     : Blocking ends
 * 2024-05-23T21:10:53.041+02:00  INFO 70538 --- [   scheduling-1] de.lathspell.test.service.FooService     : Every second
 * 2024-05-23T21:10:53.042+02:00  INFO 70538 --- [   scheduling-1] de.lathspell.test.service.FooService     : Every second
 * 2024-05-23T21:10:53.042+02:00  INFO 70538 --- [   scheduling-1] de.lathspell.test.service.FooService     : Every second
 * 2024-05-23T21:10:53.042+02:00  INFO 70538 --- [   scheduling-1] de.lathspell.test.service.FooService     : Every second
 * 2024-05-23T21:10:53.042+02:00  INFO 70538 --- [   scheduling-1] de.lathspell.test.service.FooService     : Every second
 * 2024-05-23T21:10:53.042+02:00  WARN 70538 --- [   scheduling-1] de.lathspell.test.service.FooService     : Blocking starts
 * 2024-05-23T21:10:58.043+02:00  WARN 70538 --- [   scheduling-1] de.lathspell.test.service.FooService     : Blocking ends
 * 2024-05-23T21:10:58.044+02:00  INFO 70538 --- [   scheduling-1] de.lathspell.test.service.FooService     : Every second
 * 2024-05-23T21:10:58.044+02:00  INFO 70538 --- [   scheduling-1] de.lathspell.test.service.FooService     : Every second
 * 2024-05-23T21:10:58.044+02:00  INFO 70538 --- [   scheduling-1] de.lathspell.test.service.FooService     : Every second
 * 2024-05-23T21:10:58.044+02:00  INFO 70538 --- [   scheduling-1] de.lathspell.test.service.FooService     : Every second
 * 2024-05-23T21:10:58.045+02:00  INFO 70538 --- [   scheduling-1] de.lathspell.test.service.FooService     : Every second
 * 2024-05-23T21:10:58.045+02:00  WARN 70538 --- [   scheduling-1] de.lathspell.test.service.FooService     : Blocking starts
 */
@Profile("test1")
@Component
class Test1 {

    private val log = LoggerFactory.getLogger(Test1::class.java)

    @Scheduled(fixedRateString = "PT1S")
    fun everySecond() {
        log.info("Every second")
    }

    @Scheduled(fixedRateString = "PT5S")
    fun blocker() {
        log.warn("Blocking starts")
        Thread.sleep(5000)
        log.warn("Blocking ends")
    }
}

fun main(args: Array<String>) {
    System.setProperty("spring.profiles.active", "test1")
    runApplication<App>(*args) {}
}
