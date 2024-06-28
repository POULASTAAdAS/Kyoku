plugins {
    alias(libs.plugins.kyoku.android.library)
    alias(libs.plugins.kyoku.okhttp.library)

    alias(libs.plugins.kyoku.android.dagger)
}

android {
    namespace = "com.poulastaa.setup.network"
}

dependencies {
    implementation(libs.okhttp.url.connection)

    implementation(projects.core.domain)
    implementation(projects.core.data)

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.gson)
}