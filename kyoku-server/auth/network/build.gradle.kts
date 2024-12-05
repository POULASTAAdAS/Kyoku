plugins {
    alias(libs.plugins.kyoku.ktor.library)
}

dependencies {
    implementation(project(":auth:auth-domain"))
    implementation(project(":core:core-domain"))

    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.auth)

    implementation(libs.google.auth)
    implementation(libs.redis.clients.jedis)
}