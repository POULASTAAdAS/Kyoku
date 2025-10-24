plugins {
    alias(libs.plugins.kyoku.android.library.compose)
    alias(libs.plugins.kyoku.android.dagger)
}

android {
    namespace = "com.poulastaa.core.presentation.ui"
}

dependencies {
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    implementation(libs.coil3.coil.compose)
    implementation(libs.material3WindowSizeClass)

    implementation(projects.core.domain)
    implementation(projects.core.presentation.designsystem)
    implementation(libs.lottie)
}