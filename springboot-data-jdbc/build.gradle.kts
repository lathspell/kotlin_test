import org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "2.1.21"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    kotlin("plugin.jpa") version kotlinVersion

    id("io.spring.dependency-management") version "1.1.7"
    id("org.springframework.boot") version "3.5.0"

    id("com.github.ben-manes.versions") version "0.50.0"    // https://github.com/ben-manes/gradle-versions-plugin for ":dependencyUpdates"
}

group = "de.lathspell.test"

repositories {
    mavenCentral()
}

dependencies {
    // JSON
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    // JDBC
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.flywaydb:flyway-core")
    testRuntimeOnly("com.h2database:h2")
    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.assertj:assertj-core:3.25.3")
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = JVM_21
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    // testLogging {
    // events (SKIPPED, STARTED, PASSED, FAILED)
    // exceptionFormat = TestExceptionFormat.FULL
    // showStandardStreams = true // print all stdout/stderr output to console
    // minGranularity = 0 // show class and method names
    // }
}
