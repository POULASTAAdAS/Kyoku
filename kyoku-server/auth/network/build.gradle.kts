plugins {
    alias(libs.plugins.kyoku.jvm.library)
}

dependencies {
    implementation(project(":core:core-domain"))

    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.auth)
}