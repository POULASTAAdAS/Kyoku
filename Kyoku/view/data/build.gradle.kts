plugins {
    alias(libs.plugins.kyoku.android.feature.ui)
    alias(libs.plugins.kyoku.android.dagger)
}

android {
    namespace = "com.poulastaa.view.data"
}

dependencies {
    implementation(libs.material3WindowSizeClass)

    implementation(projects.core.domain)
    implementation(projects.view.domain)

    implementation(libs.coil.compose)
    implementation(libs.pagingCompose)
}