plugins {
    alias(libs.plugins.kyoku.jvm.library)
}

dependencies {
    implementation(project(":core:core-domain"))
    implementation(project(":notification:notification-domain"))

    implementation(libs.bundles.koin)
    implementation(libs.kotlin.gson)
}