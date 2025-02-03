plugins {
    alias(libs.plugins.kyoku.ktor.exposed)
    alias(libs.plugins.kyoku.ktor.koin)
}

dependencies {
    implementation(projects.core.coreDomain)

    implementation(libs.redis.clients.jedis)
    implementation(libs.kotlin.gson)
}