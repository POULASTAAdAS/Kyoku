plugins {
    alias(libs.plugins.kyoku.android.feature.ui)
    alias(libs.plugins.kyoku.android.dagger)
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "com.poulastaa.explore.presentation"
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.material3WindowSizeClass)
    implementation(libs.navigation.compose)

    implementation(projects.core.domain)
    implementation(projects.explore.domain)
    implementation(projects.core.presentation.designsystem)

    implementation(libs.coil.compose)
    implementation(libs.pagingCompose)
    implementation(libs.pagingRuntimeKtx)

    implementation(libs.lottie)
}