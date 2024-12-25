plugins {
    alias(libs.plugins.kyoku.ktor.library)
}

dependencies {
    implementation(projects.auth.authDomain)
    implementation(projects.core.coreDomain)
    implementation(projects.core.coreNetwork)

    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.auth)

    implementation(libs.google.auth)
    implementation(libs.redis.clients.jedis)
}