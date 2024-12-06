plugins {
    alias(libs.plugins.kyoku.ktor.exposed)
    alias(libs.plugins.kyoku.ktor.koin)
}

dependencies {
    implementation(project(":core:core-domain"))

    implementation(libs.redis.clients.jedis)
    implementation(libs.kotlin.gson)
}