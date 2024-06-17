plugins {
    alias(libs.plugins.kyoku.android.library)
    alias(libs.plugins.kyoku.okhttp.library)
}

android {
    namespace = "com.poulastaa.core.data"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.database)
}