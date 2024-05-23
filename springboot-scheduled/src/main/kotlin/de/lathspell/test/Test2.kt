package de.lathspell.test

import org.slf4j.LoggerFactory
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

/**
 * Same problem as in `Test1` but the `everySecond()` method is only called once in between the `blocker()` calls when using `fixedDelay`:
 *
 * 2024-05-23T21:36:37.064+02:00  WARN 76584 --- [   scheduling-1] de.lathspell.test.service.Test2          : Blocking starts
 * 2024-05-23T21:36:42.066+02:00  WARN 76584 --- [   scheduling-1] de.lathspell.test.service.Test2          : Blocking ends
 * 2024-05-23T21:36:42.067+02:00  INFO 76584 --- [   scheduling-1] de.lathspell.test.service.Test2          : Every second
 * 2024-05-23T21:36:42.067+02:00  WARN 76584 --- [   scheduling-1] de.lathspell.test.service.Test2          : Blocking starts
 * 2024-05-23T21:36:47.072+02:00  WARN 76584 --- [   scheduling-1] de.lathspell.test.service.Test2          : Blocking ends
 * 2024-05-23T21:36:47.072+02:00  INFO 76584 --- [   scheduling-1] de.lathspell.test.service.Test2          : Every second
 * 2024-05-23T21:36:47.072+02:00  WARN 76584 --- [   scheduling-1] de.lathspell.test.service.Test2          : Blocking starts
 * 2024-05-23T21:36:52.073+02:00  WARN 76584 --- [   scheduling-1] de.lathspell.test.service.Test2          : Blocking ends
 * 2024-05-23T21:36:52.074+02:00  INFO 76584 --- [   scheduling-1] de.lathspell.test.service.Test2          : Every second
 * 2024-05-23T21:36:52.074+02:00  WARN 76584 --- [   scheduling-1] de.lathspell.test.service.Test2          : Blocking starts
 */
@Profile("test2")
@Component
class Test2 {

    private val log = LoggerFactory.getLogger(Test2::class.java)

    @Scheduled(fixedDelayString = "PT1S")
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
    System.setProperty("spring.profiles.active", "test2")
    runApplication<App>(*args)
}
