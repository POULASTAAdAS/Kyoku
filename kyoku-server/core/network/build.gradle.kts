plugins {
    alias(libs.plugins.kyoku.ktor.library)
}

dependencies {
    implementation(projects.core.coreDomain)
    implementation(projects.core.coreDatabase)

    implementation(libs.ktor.server.sessions)
    implementation(libs.bundles.koin)
    implementation(libs.kotlin.gson)
}