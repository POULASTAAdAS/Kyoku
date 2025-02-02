plugins {
    val kotlin = "2.1.0"

    kotlin("jvm") version kotlin
    id("org.jetbrains.kotlin.plugin.serialization") version kotlin apply false
}

group = "com.poulastaa.kyoku.shardmanager"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    val kotlinxCoroutines = "1.8.1"
    val kotlinxSerializationJSON = "1.7.2"

    val exposed = "0.45.0"
    val mysql = "8.0.33"
    val hikaricp = "5.0.1"
    val koin = "3.5.6"

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutines")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationJSON")

    implementation("org.jetbrains.exposed:exposed-core:$exposed")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposed")
    implementation("mysql:mysql-connector-java:$mysql")
    implementation("com.zaxxer:HikariCP:$hikaricp")

    compileOnly("io.insert-koin:koin-core:$koin")

    implementation("com.google.code.gson:gson:2.12.1")

    implementation("org.quartz-scheduler:quartz:2.5.0")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}