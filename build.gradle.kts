import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import net.ltgt.gradle.errorprone.errorprone

plugins {
    id("java")
    id("org.springframework.boot") version "3.3.0"
    id("io.spring.dependency-management") version "1.1.5"
    id("com.bmuschko.docker-java-application") version "9.4.0"
    id("com.google.osdetector") version "1.7.3"
    id("io.freefair.lombok") version "8.6"
    id("com.github.ben-manes.versions") version "0.51.0"
    id("io.gatling.gradle") version "3.10.5.1"
    id("net.ltgt.errorprone") version "3.1.0"
}

group = "io.github.mfvanek"
version = "0.2.4"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}
tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("-parameters")
    options.errorprone {
        disableWarningsInGeneratedCode.set(true)
        disable("Slf4jLoggerShouldBeNonStatic")
    }
}

repositories {
    mavenCentral()
}

dependencyManagement {
    imports {
        mavenBom("org.assertj:assertj-bom:3.26.0")
    }
}

dependencies {
    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("com.google.code.findbugs:jsr305:3.0.2")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webflux")
    testImplementation("org.assertj:assertj-core")

    // https://github.com/netty/netty/issues/11020
    if (osdetector.arch == "aarch_64") {
        testImplementation("io.netty:netty-all:4.1.110.Final")
    }

    errorprone("com.google.errorprone:error_prone_core:2.27.1")
    errorprone("jp.skypencil.errorprone.slf4j:errorprone-slf4j:0.1.24")
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
    gradleVersion = "8.7"
}

lombok {
    version = "1.18.32"
}
