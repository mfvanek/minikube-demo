import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    id("java")
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.4"
    id("com.bmuschko.docker-java-application") version "9.4.0"
    id("com.google.osdetector") version "1.7.3"
    id("io.freefair.lombok") version "8.4"
    id("com.github.ben-manes.versions") version "0.50.0"
    id("io.gatling.gradle") version "3.9.5.6"
}

group = "io.github.mfvanek"
version = "0.2.3"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}
tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("-parameters")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.micrometer:micrometer-registry-prometheus:1.12.1")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("com.google.code.findbugs:jsr305:3.0.2")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webflux")
    testImplementation("org.assertj:assertj-core:3.24.2")

    // https://github.com/netty/netty/issues/11020
    if (osdetector.arch == "aarch_64") {
        testImplementation("io.netty:netty-all:4.1.101.Final")
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

springBoot {
    buildInfo()
}

docker {
    javaApplication {
        baseImage.set("eclipse-temurin:17.0.7_7-jre-focal") // G1GC by default everywhere
        // baseImage.set("amazoncorretto:17.0.6")
        // baseImage.set("azul/zulu-openjdk:17.0.5")
        // baseImage.set("bellsoft/liberica-openjdk-alpine:17.0.7-7")
        maintainer.set("Ivan Vakhrushev")
        jvmArgs.set(listOf("-XX:+PrintFlagsFinal", "-XX:ActiveProcessorCount=2"))
        images.set(
            listOf(
                "${project.group}/${project.name}:${project.version}",
                "${project.group}/${project.name}:latest"
            )
        )
    }
}

fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.uppercase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}

tasks.named<DependencyUpdatesTask>("dependencyUpdates").configure {
    checkForGradleUpdate = true
    gradleReleaseChannel = "current"
    checkConstraints = true
    rejectVersionIf {
        isNonStable(candidate.version)
    }
}

tasks.bootRun {
    if (project.hasProperty("jvmArgs")) {
        val jvmArgsFromCommandLine = project.properties["jvmArgs"].toString().split("\\s".toRegex())
        jvmArgs = jvmArgsFromCommandLine
    }
}

gatling {
    logHttp = io.gatling.gradle.LogHttp.FAILURES
}

tasks.wrapper {
    gradleVersion = "8.5"
}
