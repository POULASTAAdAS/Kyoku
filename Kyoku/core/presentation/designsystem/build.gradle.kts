plugins {
    alias(libs.plugins.kyoku.android.library.compose)
}

android {
    namespace = "com.poulastaa.core.presentation.designsystem"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    api(libs.androidx.material3)

    implementation(libs.material3WindowSizeClass)

    debugImplementation(libs.androidx.ui.tooling)
}