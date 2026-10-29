plugins {
    alias(libs.plugins.kyoku.kmp.common.ui)
    alias(libs.plugins.kotlinSerializationPlugin)
}

android {
    namespace = "com.poulastaa.common.ui"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.common.viewmodels)
        }
    }
}
