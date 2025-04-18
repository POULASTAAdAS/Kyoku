plugins {
    alias(libs.plugins.kyoku.android.library)
    alias(libs.plugins.kyoku.okhttp.library)

    alias(libs.plugins.kyoku.android.dagger)
}

android {
    namespace = "com.poulastaa.add.data"
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.gson)
    implementation(libs.pagingCommon)

    implementation(projects.core.domain)
    implementation(projects.add.domain)
}