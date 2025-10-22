plugins {
    alias(libs.plugins.kyoku.android.application.compose)
    alias(libs.plugins.secrets.gradle.plugin)
    alias(libs.plugins.kyoku.okhttp.library)
    alias(libs.plugins.kyoku.android.dagger)
}

android {
    namespace = "com.poulastaa.kyoku"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)

    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    implementation(libs.kotlinx.serialization.json)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    implementation(libs.gson)
    implementation(libs.datastore.preferences)

    // splash
    implementation(libs.core.splash.screen)

    // compose
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.navigation.compose)
    implementation(libs.material3WindowSizeClass)
    implementation(libs.google.id)

    // navigation
    implementation(libs.navigation.compose)

    // work
    implementation(libs.androidx.work)
    implementation(libs.hilt.work)

    // project
    implementation(projects.core.domain)
    implementation(projects.core.data)
    implementation(projects.core.database)
    implementation(projects.core.network)

    implementation(projects.core.presentation.designsystem)
    implementation(projects.core.presentation.ui)

    implementation(projects.auth.domain)
    implementation(projects.auth.data)
    implementation(projects.auth.presentation)
    implementation(projects.auth.network)

    implementation(projects.board.domain)
    implementation(projects.board.data)
    implementation(projects.board.presentation)
    implementation(projects.board.network)
}