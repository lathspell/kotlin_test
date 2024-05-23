package de.lathspell.test

import org.slf4j.LoggerFactory
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

/**
 * Scheduling a method to run every second although the method takes 5s to finish.
 *
 * Works using "fixedDelay" only gets scheduled after the last execution is finished:
 *
 * 2024-05-23T21:54:46.852+02:00  WARN 80617 --- [   scheduling-1] de.lathspell.test.Test2                  : Worker starts
 * 2024-05-23T21:54:51.853+02:00  WARN 80617 --- [   scheduling-1] de.lathspell.test.Test2                  : Worker ends
 * 2024-05-23T21:54:52.858+02:00  WARN 80617 --- [   scheduling-1] de.lathspell.test.Test2                  : Worker starts
 * 2024-05-23T21:54:57.859+02:00  WARN 80617 --- [   scheduling-1] de.lathspell.test.Test2                  : Worker ends
 * 2024-05-23T21:54:58.864+02:00  WARN 80617 --- [   scheduling-1] de.lathspell.test.Test2                  : Worker starts
 */
@Profile("test4")
@Component
class Test4 {

    private val log = LoggerFactory.getLogger(Test4::class.java)

    @Scheduled(fixedDelayString = "PT1S")
    fun worker() {
        log.warn("Worker starts")
        Thread.sleep(5000)
        log.warn("Worker ends")
    }

}

fun main(args: Array<String>) {
    System.setProperty("spring.profiles.active", "test4")
    runApplication<App>(*args)
}
