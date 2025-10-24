plugins {
    alias(libs.plugins.kyoku.android.feature.ui)
    alias(libs.plugins.kyoku.android.dagger)
}

android {
    namespace = "com.poulastaa.board.presentation"
}

dependencies {
    implementation(libs.material3WindowSizeClass)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.coil3.coil.compose)

    implementation(projects.core.domain)
    implementation(projects.board.domain)

    debugImplementation(libs.androidx.compose.ui.tooling)
}