plugins {
  id("java")
  id("org.springframework.boot") version "3.1.0"
  id("io.spring.dependency-management") version "1.1.0"
  id("com.bmuschko.docker-java-application") version "9.3.1"
  id("com.google.osdetector") version "1.7.3"
  id("io.freefair.lombok") version "8.0.1"
}

group = "io.github.mfvanek"
version = "0.2.0"

java {
  sourceCompatibility = JavaVersion.VERSION_17
  targetCompatibility = JavaVersion.VERSION_17
}

repositories {
  mavenCentral()
}

dependencies {
  implementation("io.micrometer:micrometer-registry-prometheus:1.11.0")
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  implementation("com.google.code.findbugs:jsr305:3.0.2")

  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("org.springframework.boot:spring-boot-starter-webflux")
  testImplementation("org.assertj:assertj-core:3.24.2")

  // https://github.com/netty/netty/issues/11020
  if (osdetector.arch == "aarch_64") {
    testImplementation("io.netty:netty-all:4.1.92.Final")
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
    baseImage.set("eclipse-temurin:17.0.7_7-jre-focal")
    maintainer.set("Ivan Vakhrushev")
    images.set(listOf("${project.group}/${project.name}:${project.version}", "${project.group}/${project.name}:latest"))
  }
}
