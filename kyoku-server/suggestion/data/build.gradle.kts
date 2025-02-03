plugins {
    alias(libs.plugins.kyoku.jvm.library)
    alias(libs.plugins.kyoku.ktor.koin)
}

dependencies {
    implementation(projects.core.coreDomain)
    implementation(projects.suggestion.suggestionDomain)
}