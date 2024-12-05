plugins {
    alias(libs.plugins.kyoku.jvm.library)
}

dependencies {
    implementation(project(":core:core-domain"))
    implementation(project(":auth:auth-domain"))

    implementation(libs.bundles.koin)
}