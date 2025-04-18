plugins {
    alias(libs.plugins.kyoku.android.library)
    alias(libs.plugins.kyoku.okhttp.library)

    alias(libs.plugins.kyoku.android.dagger)
}

android {
    namespace = "com.poulastaa.main.data"
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.gson)

    implementation(projects.core.domain)
    implementation(projects.main.domain)

    implementation(libs.pagingCommon)
    implementation(libs.androidx.work)
    implementation(libs.hilt.work)
}