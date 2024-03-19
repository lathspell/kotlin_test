import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "1.9.23"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    kotlin("plugin.jpa") version kotlinVersion

    id("com.github.ben-manes.versions") version "0.51.0"        // https://github.com/ben-manes/gradle-versions-plugin for ":dependencyUpdates"
    id("io.spring.dependency-management") version "1.1.0"
    id("org.springframework.boot") version "3.2.3"
}

group = "de.lathspell.test"

repositories {
    mavenCentral()
}

dependencies {
    // JSON
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    // JPA
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    testRuntimeOnly("com.h2database:h2")
    testRuntimeOnly("org.postgresql:postgresql:42.7.3")
    implementation("org.flywaydb:flyway-core:9.22.3")
    testImplementation("org.flywaydb.flyway-test-extensions:flyway-spring-test:9.5.0")
    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
}

kotlin {
    jvmToolchain(17)
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
