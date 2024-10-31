import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent.*

plugins {
    kotlin("jvm") version "2.0.20"

    id("com.github.ben-manes.versions") version "0.51.0"        // https://github.com/ben-manes/gradle-versions-plugin for ":dependencyUpdates"
}

group = "de.lathspell"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    // Kotlin
    implementation(kotlin("reflect"))

    // JSON
    val jacksonVersion = "2.18.0"
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")

    // JSON Patch
    implementation("com.flipkart.zjsonpatch:zjsonpatch:0.4.16")

    // Test
    testImplementation(platform("org.junit:junit-bom:5.10.3"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

kotlin {
    jvmToolchain(17)
    compilerOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
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
