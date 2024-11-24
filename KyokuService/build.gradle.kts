val kotlin_version: String by project
val logback_version: String by project

val exposed_version: String by project
val mysql_version: String by project


val koin_ktor: String by project
val hikaricp_version: String by project

plugins {
    kotlin("jvm") version "2.0.0"
    id("io.ktor.plugin") version "2.3.12"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.0"
}

group = "com.poulastaa"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm")

    implementation("io.ktor:ktor-server-auth-jvm")
    implementation("io.ktor:ktor-server-sessions-jvm")
    implementation("io.ktor:ktor-server-auth-jwt-jvm")

    implementation("io.ktor:ktor-server-call-logging-jvm")

    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")

    // db
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposed_version")
    //connection pooling
    implementation("com.zaxxer:HikariCP:$hikaricp_version")
    implementation("mysql:mysql-connector-java:$mysql_version")

    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:$logback_version")


    // spotify web api
    implementation("se.michaelthelin.spotify:spotify-web-api-java:8.3.4")

    // koin
    implementation("io.insert-koin:koin-ktor:$koin_ktor")

    // client
    implementation("io.ktor:ktor-client-core-jvm")
    implementation("io.ktor:ktor-client-apache-jvm")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.2")
}
