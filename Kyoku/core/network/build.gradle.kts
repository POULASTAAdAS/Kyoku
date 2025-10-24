plugins {
    alias(libs.plugins.kyoku.android.library)
    alias(libs.plugins.kyoku.okhttp.library)

    alias(libs.plugins.kyoku.android.dagger)
}


android {
    namespace = "com.poulastaa.core.network"
}

dependencies {
    implementation(libs.okhttp.url.connection)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.gson)

    implementation(libs.coil3.network.okhttp)

    implementation(projects.core.domain)
}