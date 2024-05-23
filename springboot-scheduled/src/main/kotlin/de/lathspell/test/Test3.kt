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
 * Using a bigger thread pool, the output is now as expected:
 *
 * 2024-05-23T21:39:28.214+02:00  INFO 77256 --- [pool-4-thread-2] de.lathspell.test.service.Test2          : Every second
 * 2024-05-23T21:39:28.214+02:00  WARN 77256 --- [pool-4-thread-1] de.lathspell.test.service.Test2          : Blocking starts
 * 2024-05-23T21:39:29.220+02:00  INFO 77256 --- [pool-4-thread-2] de.lathspell.test.service.Test2          : Every second
 * 2024-05-23T21:39:30.222+02:00  INFO 77256 --- [pool-4-thread-3] de.lathspell.test.service.Test2          : Every second
 * 2024-05-23T21:39:31.227+02:00  INFO 77256 --- [pool-4-thread-2] de.lathspell.test.service.Test2          : Every second
 * 2024-05-23T21:39:32.230+02:00  INFO 77256 --- [pool-4-thread-2] de.lathspell.test.service.Test2          : Every second
 * 2024-05-23T21:39:33.216+02:00  WARN 77256 --- [pool-4-thread-1] de.lathspell.test.service.Test2          : Blocking ends
 * 2024-05-23T21:39:33.231+02:00  INFO 77256 --- [pool-4-thread-2] de.lathspell.test.service.Test2          : Every second
 * 2024-05-23T21:39:34.236+02:00  INFO 77256 --- [pool-4-thread-2] de.lathspell.test.service.Test2          : Every second
 * 2024-05-23T21:39:35.237+02:00  INFO 77256 --- [pool-4-thread-2] de.lathspell.test.service.Test2          : Every second
 * 2024-05-23T21:39:36.241+02:00  INFO 77256 --- [pool-4-thread-2] de.lathspell.test.service.Test2          : Every second
 * 2024-05-23T21:39:37.243+02:00  INFO 77256 --- [pool-4-thread-2] de.lathspell.test.service.Test2          : Every second
 * 2024-05-23T21:39:38.221+02:00  WARN 77256 --- [pool-4-thread-3] de.lathspell.test.service.Test2          : Blocking starts
 * 2024-05-23T21:39:38.248+02:00  INFO 77256 --- [pool-4-thread-5] de.lathspell.test.service.Test2          : Every second
 */
@Profile("test3")
@Component
class Test3 {

    private val log = LoggerFactory.getLogger(Test3::class.java)

    @Scheduled(fixedDelayString = "PT1S")
    fun everySecond() {
        log.info("Every second")
    }

    @Scheduled(fixedDelayString = "PT5S")
    fun blocker() {
        log.warn("Blocking starts")
        Thread.sleep(5000)
        log.warn("Blocking ends")
    }
}

@Profile("test3")
@Configuration
class Test3Config : SchedulingConfigurer {

    @Override
    override fun configureTasks(taskRegistrar: ScheduledTaskRegistrar) {
        taskRegistrar.setScheduler(Executors.newScheduledThreadPool(5));
    }
}

fun main(args: Array<String>) {
    System.setProperty("spring.profiles.active", "test3")
    runApplication<App>(*args)
}
