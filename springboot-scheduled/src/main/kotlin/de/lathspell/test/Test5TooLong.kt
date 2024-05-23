package de.lathspell.test

import org.slf4j.LoggerFactory
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.scheduling.annotation.SchedulingConfigurer
import org.springframework.scheduling.config.ScheduledTaskRegistrar
import org.springframework.stereotype.Component
import java.util.concurrent.Executors

/**
 * Scheduling a method to run every second although the method takes 5s to finish.
 *
 * Apparently even works with fixedRate and even with a larger thread pool.
 *
 * 2024-05-23T21:56:26.396+02:00  WARN 81036 --- [pool-4-thread-1] de.lathspell.test.Test2                  : Worker starts
 * 2024-05-23T21:56:31.397+02:00  WARN 81036 --- [pool-4-thread-1] de.lathspell.test.Test2                  : Worker ends
 * 2024-05-23T21:56:31.398+02:00  WARN 81036 --- [pool-4-thread-1] de.lathspell.test.Test2                  : Worker starts
 * 2024-05-23T21:56:36.402+02:00  WARN 81036 --- [pool-4-thread-1] de.lathspell.test.Test2                  : Worker ends
 * 2024-05-23T21:56:36.402+02:00  WARN 81036 --- [pool-4-thread-2] de.lathspell.test.Test2                  : Worker starts
 * 2024-05-23T21:56:41.405+02:00  WARN 81036 --- [pool-4-thread-2] de.lathspell.test.Test2                  : Worker ends
 * 2024-05-23T21:56:41.406+02:00  WARN 81036 --- [pool-4-thread-1] de.lathspell.test.Test2                  : Worker starts
 * 2024-05-23T21:56:46.411+02:00  WARN 81036 --- [pool-4-thread-1] de.lathspell.test.Test2                  : Worker ends
 * 2024-05-23T21:56:46.412+02:00  WARN 81036 --- [pool-4-thread-3] de.lathspell.test.Test2                  : Worker starts
 */
@Profile("test5")
@Component
class Test5 {

    private val log = LoggerFactory.getLogger(Test5::class.java)

    @Scheduled(fixedRateString = "PT1S")
    fun worker() {
        log.warn("Worker starts")
        Thread.sleep(5000)
        log.warn("Worker ends")
    }
}

@Profile("test5")
@Configuration
class Test5Config : SchedulingConfigurer {

    @Override
    override fun configureTasks(taskRegistrar: ScheduledTaskRegistrar) {
        taskRegistrar.setScheduler(Executors.newScheduledThreadPool(5));
    }
}

fun main(args: Array<String>) {
    System.setProperty("spring.profiles.active", "test5")
    runApplication<App>(*args)
}
