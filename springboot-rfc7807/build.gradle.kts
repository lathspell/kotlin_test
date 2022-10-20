import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    val kotlinVersion = "1.7.20"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion

    id("io.spring.dependency-management") version "1.1.0"
    id("org.springframework.boot") version "3.0.0-M4"

    id("com.github.ben-manes.versions") version "0.43.0"
}

repositories {
    mavenCentral()
    maven(url = "https://repo.spring.io/milestone/")
}

dependencies {
    // Spring
    val springVersion = "3.0.0-M4"
    implementation("org.springframework.boot:spring-boot-starter-web:$springVersion")
    // JSON
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.4")
    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test:$springVersion")
}

fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().contains(it) }
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
