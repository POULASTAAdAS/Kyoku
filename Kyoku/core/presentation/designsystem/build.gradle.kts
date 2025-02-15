plugins {
    alias(libs.plugins.kyoku.android.library.compose)
}

android {
    namespace = "com.poulastaa.core.presentation.designsystem"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    api(libs.androidx.material3)
    implementation(libs.material3WindowSizeClass)

    implementation(projects.core.domain)

    debugImplementation(libs.androidx.ui.tooling)
}