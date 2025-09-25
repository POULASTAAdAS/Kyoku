plugins {
    alias(libs.plugins.kyoku.android.feature.ui)
    alias(libs.plugins.kyoku.android.dagger)
}

android {
    namespace = "com.poulastaa.auth.presentation"
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.credentials)
    implementation(libs.credentialsPlayServicesAuth)
    implementation(libs.google.id)
    implementation(libs.material3WindowSizeClass)

    implementation(projects.core.domain)
    implementation(projects.auth.domain)

}