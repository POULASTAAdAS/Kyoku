plugins {
    alias(libs.plugins.kyoku.android.library.compose)
    alias(libs.plugins.kyoku.android.dagger)
}

android {
    namespace = "com.poulastaa.core.presentation.designsystem"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    api(libs.androidx.material3)
    implementation(libs.material3WindowSizeClass)
    implementation(libs.coil.compose)
    implementation(libs.pagingCompose)

    implementation(projects.core.domain)

    debugImplementation(libs.androidx.ui.tooling)

}