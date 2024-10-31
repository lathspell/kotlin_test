import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "2.0.20"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    kotlin("plugin.jpa") version kotlinVersion
    kotlin("kapt") version kotlinVersion // for QueryDSL
    id("com.github.ben-manes.versions") version "0.51.0"        // https://github.com/ben-manes/gradle-versions-plugin for ":dependencyUpdates"
    id("io.spring.dependency-management") version "1.1.6"
    id("org.springframework.boot") version "3.3.5"
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
    testRuntimeOnly("org.postgresql:postgresql:42.7.4")
    // Flyway
    val flywayVersion = "10.20.1"
    implementation("org.flywaydb:flyway-core:$flywayVersion")
    implementation("org.flywaydb:flyway-database-postgresql:$flywayVersion")
    // QueryDSL
    val queryDslVersion = "6.8"
    implementation("io.github.openfeign.querydsl:querydsl-jpa:${queryDslVersion}")
    implementation("io.github.openfeign.querydsl:querydsl-apt:${queryDslVersion}:jakarta")
    kapt("io.github.openfeign.querydsl:querydsl-apt:${queryDslVersion}:jakarta")
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
