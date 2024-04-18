val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
//val junit_version: String by project

plugins {
    kotlin("jvm") version "1.9.21"
    id("io.ktor.plugin") version "2.3.6"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.21"
}

group = "com.example"
version = "0.0.1"

application {
    mainClass.set("com.example.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-apache:$ktor_version")
    testImplementation("io.ktor:ktor-client-mock:$ktor_version")
//    testImplementation("junit:junit:$junit_version")

    implementation("javazoom:jlayer:1.0.1")

    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.ktor:ktor-client-serialization:$ktor_version")

    implementation("com.upokecenter:cbor:4.5.2")
    implementation("io.ktor:ktor-network-tls-certificates:$ktor_version")
//    implementation("org.json:json:20231013")

}
