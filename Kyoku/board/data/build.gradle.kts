plugins {
    alias(libs.plugins.kyoku.android.library)
    alias(libs.plugins.kyoku.okhttp.library)

    alias(libs.plugins.kyoku.android.dagger)
}

android {
    namespace = "com.poulastaa.board.data"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.board.domain)
    implementation(projects.core.data)
}