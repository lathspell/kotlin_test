import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    kotlin("jvm") version "1.9.21"
    kotlin("plugin.spring") version "1.9.21"
    id("io.spring.dependency-management") version "1.1.4"
    id("org.springframework.boot") version "3.2.0"

    id("com.github.ben-manes.versions") version "0.50.0"
}

group = "de.lathspell.test"

repositories {
    mavenCentral()
}

dependencies {
    val springVersion = "3.2.0"

    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.21")
    // Kotlin Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.7.3")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:1.1.4")
    // JSON
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.16.0")
    // Webflux
    implementation("org.springframework.boot:spring-boot-starter-webflux:$springVersion")
    // KTor HTTP Client Facade
    val ktorVersion = "1.6.3"
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-apache:$ktorVersion")
    implementation("io.ktor:ktor-client-logging:$ktorVersion")
    implementation("io.ktor:ktor-client-auth:$ktorVersion")
    implementation("io.ktor:ktor-client-jackson:$ktorVersion")
    // OkHttp
    val okhttpVersion = "4.9.1"
    implementation("com.squareup.okhttp3:okhttp:$okhttpVersion")
    // Fuel
    val fuelVersion = "2.3.1"
    implementation("com.github.kittinunf.fuel:fuel:$fuelVersion")
    implementation("com.github.kittinunf.fuel:fuel-jackson:$fuelVersion")
    implementation("com.github.kittinunf.fuel:fuel-coroutines:$fuelVersion")
    // http4k
    implementation("org.http4k:http4k-core:4.13.0.0")
    implementation("org.http4k:http4k-client-apache4:4.13.0.0")
    implementation("org.http4k:http4k-format-jackson:4.13.0.0")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test:$springVersion")
    testImplementation("io.projectreactor:reactor-test:3.4.10")
}

fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.uppercase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}

// https://github.com/ben-manes/gradle-versions-plugin
tasks.withType<DependencyUpdatesTask> {
    rejectVersionIf {
        isNonStable(candidate.version) && !isNonStable(currentVersion)
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
