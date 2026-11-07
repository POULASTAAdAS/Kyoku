plugins {
    alias(libs.plugins.kyoku.kmp.feature.ui)
}

kotlin {
    androidLibrary {
        namespace = "com.poulastaa.setup.ui"
    }
    sourceSets {
        commonMain.dependencies {
            implementation(projects.setup.viewmodels)
        }
    }
}
