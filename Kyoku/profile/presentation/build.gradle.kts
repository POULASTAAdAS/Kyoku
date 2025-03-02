plugins {
    alias(libs.plugins.kyoku.android.feature.ui)
    alias(libs.plugins.kyoku.android.dagger)
}
android {
    namespace = "com.poulastaa.profile.presentation"
}

dependencies {
    implementation(libs.material3WindowSizeClass)

    implementation(libs.coil.compose)

    implementation(projects.core.domain)
    implementation(projects.profile.domain)
}