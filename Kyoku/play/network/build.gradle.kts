plugins {
    alias(libs.plugins.kyoku.android.library)
    alias(libs.plugins.kyoku.okhttp.library)

    alias(libs.plugins.kyoku.android.dagger)
}

android {
    namespace = "com.poulastaa.play.network"
}

dependencies {
    implementation(libs.okhttp.url.connection)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.gson)

    implementation(libs.pagingRuntimeKtx)
    implementation(libs.pagingCompose)

    implementation(projects.core.domain)
    implementation(projects.core.data)
}