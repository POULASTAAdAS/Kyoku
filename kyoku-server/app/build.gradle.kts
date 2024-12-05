plugins {
    alias(libs.plugins.kyoku.ktor.application)
}

dependencies {
    implementation(project(":core:core-domain"))
    implementation(project(":auth:auth-domain"))
    implementation(project(":details:details-domain"))
    implementation(project(":play:play-domain"))
    implementation(project(":search:search-domain"))
    implementation(project(":suggestion:suggestion-domain"))
    implementation(project(":user:user-domain"))

    implementation(project(":auth:auth-network"))

    implementation(project(":auth:auth-data"))
    implementation(project(":core:core-data"))

    implementation(project(":core:core-database"))

    implementation(libs.bundles.koin)
    implementation(libs.redis.clients.jedis)
}