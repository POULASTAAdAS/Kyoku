plugins {
    alias(libs.plugins.kyoku.android.library)

    alias(libs.plugins.kyoku.okhttp.library)
    alias(libs.plugins.kyoku.android.dagger)
}

android {
    namespace = "com.poulastaa.play.data"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.gson)
    implementation(libs.pagingCommon)
    implementation(libs.androidx.work)
    implementation(libs.hilt.work)

    implementation(libs.media3.exoplayer)
    implementation(libs.media3.exoplayer.hls)
    implementation(libs.media3.session)

    implementation(projects.core.domain)
    implementation(projects.play.domain)
    implementation(projects.core.database)
}