import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"

    id("com.github.ben-manes.versions") version "0.42.0"        // https://github.com/ben-manes/gradle-versions-plugin for ":dependencyUpdates"
}

group = "de.lathspell"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    jcenter()
}

dependencies {
    // Kotlin
    implementation(kotlin("reflect"))

    // Test
    testImplementation(platform("org.junit:junit-bom:5.7.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:3.11.1")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()

    outputs.upToDateWhen { false } // Tests immer ausführen!
    testLogging {
        events(SKIPPED, STARTED, PASSED, FAILED)
        exceptionFormat = TestExceptionFormat.FULL
        showStandardStreams = true // print all stdout/stderr output to console
        minGranularity = 0 // show class and method names
    }
}
