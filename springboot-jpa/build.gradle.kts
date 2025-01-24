import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "2.1.0"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    kotlin("plugin.jpa") version kotlinVersion

    id("com.github.ben-manes.versions") version "0.52.0"        // https://github.com/ben-manes/gradle-versions-plugin for ":dependencyUpdates"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.springframework.boot") version "3.4.2"
}

group = "de.lathspell.test"

repositories {
    mavenCentral()
}

dependencies {
    // JSON
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    // SQL
    testRuntimeOnly("com.h2database:h2")
    testRuntimeOnly("org.postgresql:postgresql:42.7.5")
    val flywayVersion = "10.20.1"
    runtimeOnly("org.flywaydb:flyway-core:$flywayVersion")
    runtimeOnly("org.flywaydb:flyway-database-postgresql:$flywayVersion")
    testImplementation("org.flywaydb.flyway-test-extensions:flyway-spring-test:10.0.0")
    // JPA
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
}

kotlin {
    jvmToolchain(17)
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        freeCompilerArgs.add("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
