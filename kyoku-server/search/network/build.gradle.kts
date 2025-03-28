plugins {
    alias(libs.plugins.kyoku.ktor.library)
}

dependencies {
    implementation(projects.search.searchDomain)
    implementation(projects.core.coreDomain)
    implementation(projects.core.coreNetwork)

    implementation(libs.ktor.server.core)
    implementation(libs.spotify.api)
    implementation(libs.ktor.server.auth)

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.jvm)
}