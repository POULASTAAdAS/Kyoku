plugins {
    alias(libs.plugins.kyoku.android.feature.ui)
    alias(libs.plugins.kyoku.android.dagger)
}


android {
    namespace = "com.poulastaa.main.presentation"
}

dependencies {
    implementation(libs.material3WindowSizeClass)

    implementation(projects.core.domain)
    implementation(projects.main.domain)

    implementation(libs.coil.compose)
    implementation(libs.pagingCompose)
}