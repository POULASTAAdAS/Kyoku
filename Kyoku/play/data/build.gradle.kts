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

    implementation(projects.core.domain)
    implementation(projects.play.domain)
    implementation(projects.core.database)
}