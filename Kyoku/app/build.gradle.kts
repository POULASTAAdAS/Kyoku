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
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    implementation(libs.kotlinx.serialization.json)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

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

    // project
    implementation(projects.core.domain)
    implementation(projects.core.data)
    implementation(projects.core.database)

    implementation(projects.core.presentation.designsystem)
    implementation(projects.core.presentation.ui)

    implementation(projects.auth.domain)
    implementation(projects.auth.data)
    implementation(projects.auth.network)
    implementation(projects.auth.presentation)

    implementation(projects.settings.domain)
    implementation(projects.settings.data)
    implementation(projects.settings.network)
    implementation(projects.settings.presentation)

    implementation(projects.setup.domain)
    implementation(projects.setup.data)
    implementation(projects.setup.network)
    implementation(projects.setup.presentation)

    implementation(projects.main.domain)
    implementation(projects.main.data)
    implementation(projects.main.network)
    implementation(projects.main.presentation)

    implementation(projects.profile.domain)
    implementation(projects.profile.data)
    implementation(projects.profile.netwokr)
    implementation(projects.profile.presentation)
}