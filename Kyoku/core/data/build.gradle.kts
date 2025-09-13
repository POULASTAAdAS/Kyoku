plugins {
    alias(libs.plugins.kyoku.android.library)
    alias(libs.plugins.kyoku.android.dagger)
}

android {
    namespace = "com.poulastaa.core.data"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.database)

    implementation(libs.gson)

    implementation(libs.androidx.palette)
    implementation(libs.datastore.preferences)
}