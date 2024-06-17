plugins {
    alias(libs.plugins.kyoku.android.library)
}

android {
    namespace = "com.poulastaa.play.data"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.serialization.json)

    implementation(projects.core.domain)
    implementation(projects.play.domain)
    implementation(projects.core.database)
}