plugins {
    alias(libs.plugins.kyoku.android.feature.ui)
    alias(libs.plugins.kyoku.android.dagger)
}

android {
    namespace = "com.poulastaa.setup.presentation"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.setup.domain)

    implementation(libs.material3WindowSizeClass)
    implementation(libs.coil.compose)
}