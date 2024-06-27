plugins {
    alias(libs.plugins.kyoku.android.library)
    alias(libs.plugins.kyoku.okhttp.library)

    alias(libs.plugins.kyoku.android.dagger)
}

android {
    namespace = "com.poulastaa.setup.data"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.setup.domain)
    implementation(projects.core.database)
    implementation(projects.core.data)

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.gson)
}