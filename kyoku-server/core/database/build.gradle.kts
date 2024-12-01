plugins {
    alias(libs.plugins.kyoku.ktor.exposed)
    alias(libs.plugins.kyoku.ktor.koin)
}

dependencies {
    implementation(project(":core:domain"))
}