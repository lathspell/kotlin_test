package de.lathspell.test

import org.slf4j.LoggerFactory
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.scheduling.annotation.SchedulingConfigurer
import org.springframework.scheduling.config.ScheduledTaskRegistrar
import org.springframework.stereotype.Component
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

/**
 * Scheduling a method to run every second although the method takes 5s to finish.
 *
 * Using @Async (don't forget @EnableAsync) an execution may start, even if the last has not yet finished.
 *
 * 2024-05-23T22:14:35.083+02:00  WARN 84909 --- [         task-1] de.lathspell.test.Test6                  : Worker 1 starts
 * 2024-05-23T22:14:36.079+02:00  WARN 84909 --- [         task-2] de.lathspell.test.Test6                  : Worker 2 starts
 * 2024-05-23T22:14:37.080+02:00  WARN 84909 --- [         task-3] de.lathspell.test.Test6                  : Worker 3 starts
 * 2024-05-23T22:14:38.078+02:00  WARN 84909 --- [         task-4] de.lathspell.test.Test6                  : Worker 4 starts
 * 2024-05-23T22:14:39.078+02:00  WARN 84909 --- [         task-5] de.lathspell.test.Test6                  : Worker 5 starts
 * 2024-05-23T22:14:40.078+02:00  WARN 84909 --- [         task-6] de.lathspell.test.Test6                  : Worker 6 starts
 * 2024-05-23T22:14:40.085+02:00  WARN 84909 --- [         task-1] de.lathspell.test.Test6                  : Worker 1 ends
 * 2024-05-23T22:14:41.078+02:00  WARN 84909 --- [         task-7] de.lathspell.test.Test6                  : Worker 7 starts
 * 2024-05-23T22:14:41.084+02:00  WARN 84909 --- [         task-2] de.lathspell.test.Test6                  : Worker 2 ends
 * 2024-05-23T22:14:42.081+02:00  WARN 84909 --- [         task-8] de.lathspell.test.Test6                  : Worker 8 starts
 * 2024-05-23T22:14:42.084+02:00  WARN 84909 --- [         task-3] de.lathspell.test.Test6                  : Worker 3 ends
 */
@Profile("test6")
@Component
class Test6 {

    private val log = LoggerFactory.getLogger(Test6::class.java)

    private val counter: AtomicInteger = AtomicInteger(0)

    @Async
    @Scheduled(fixedRateString = "PT1S")
    fun worker() {
        val id = counter.incrementAndGet()
        log.warn("Worker $id starts")
        Thread.sleep(5000)
        log.warn("Worker $id ends")
    }
}

@Profile("test6")
@Configuration
@EnableAsync
class Test6Config : SchedulingConfigurer {

    @Override
    override fun configureTasks(taskRegistrar: ScheduledTaskRegistrar) {
        taskRegistrar.setScheduler(Executors.newScheduledThreadPool(5));
    }
}

fun main(args: Array<String>) {
    System.setProperty("spring.profiles.active", "test6")
    runApplication<App>(*args)
}
