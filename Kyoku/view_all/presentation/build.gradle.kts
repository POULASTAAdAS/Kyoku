plugins {
    alias(libs.plugins.kyoku.android.feature.ui)
    alias(libs.plugins.kyoku.android.dagger)
}

android {
    namespace = "com.poulastaa.view_all.presentation"
}

dependencies {
    implementation(libs.material3WindowSizeClass)


    implementation(projects.core.domain)
    implementation(projects.viewAll.domain)
    implementation(projects.core.presentation.ui)

    implementation(libs.coil.compose)
    implementation(libs.pagingCompose)

    implementation(libs.lottie)
}