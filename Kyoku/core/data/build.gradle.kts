plugins {
    alias(libs.plugins.kyoku.android.library)
    alias(libs.plugins.kyoku.okhttp.library)
    alias(libs.plugins.kyoku.android.dagger)
}

android {
    namespace = "com.poulastaa.core.data"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.database)

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.gson)

    implementation(libs.datastore.preferences)
}