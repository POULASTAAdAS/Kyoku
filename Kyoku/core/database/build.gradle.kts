plugins {
    alias(libs.plugins.kyoku.android.library)
    alias(libs.plugins.kyoku.android.room)
}

android {
    namespace = "com.poulastaa.core.database"
}

dependencies {
    implementation(projects.core.domain)
}