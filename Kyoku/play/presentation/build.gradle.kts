plugins {
    alias(libs.plugins.kyoku.android.feature.ui)
    alias(libs.plugins.kyoku.android.dagger)
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "com.poulastaa.play.presentation"
}

dependencies {
    implementation(libs.navigation.compose)
    implementation(libs.coil.compose)
    implementation(libs.androidx.activity.compose)
    implementation(libs.material3WindowSizeClass)

    implementation(libs.compose.foundation)
    implementation(libs.pagingRuntimeKtx)
    implementation(libs.pagingCompose)

    implementation(libs.kotlinx.serialization.json)

    implementation(projects.core.domain)
    implementation(projects.play.domain)
}