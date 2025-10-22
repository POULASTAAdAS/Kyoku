plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.5.5"
    id("io.spring.dependency-management") version "1.1.7"
    kotlin("plugin.jpa") version "1.9.25"
}

group = "com.poulastaa.kyoku"
version = "1.0.0"
description = "auth"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

extra["springCloudVersion"] = "2025.0.0"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // google one-tap
    implementation("com.google.api-client:google-api-client:2.8.1")

    // service discovery
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")

    // security
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    // sql
    runtimeOnly("com.mysql:mysql-connector-j")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // jwt
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")

    // config
    implementation("org.springframework.cloud:spring-cloud-starter-config")

    // devtools
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // rabbitMQ
    implementation("org.springframework.boot:spring-boot-starter-amqp")

    // redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // jwt
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
