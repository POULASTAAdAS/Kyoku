plugins {
    alias(libs.plugins.kyoku.android.feature.ui)
    alias(libs.plugins.kyoku.android.dagger)
    alias(libs.plugins.kotlinx.serialization)
}


android {
    namespace = "com.poulastaa.main.presentation"
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.material3WindowSizeClass)
    implementation(libs.navigation.compose)

    implementation(projects.core.domain)
    implementation(projects.main.domain)

    implementation(libs.coil.compose)
    implementation(libs.pagingCompose)
}