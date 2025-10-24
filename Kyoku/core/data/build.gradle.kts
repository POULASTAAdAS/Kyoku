plugins {
    alias(libs.plugins.kyoku.android.library)
    alias(libs.plugins.kyoku.android.dagger)
}

android {
    namespace = "com.poulastaa.core.data"
}

dependencies {

    implementation(libs.gson)
    implementation(libs.coil3.core)
    implementation(libs.coil3.coil.compose)
    implementation(libs.androidx.palette)
    implementation(libs.datastore.preferences)

    implementation(projects.core.database)
    implementation(projects.core.domain)
}