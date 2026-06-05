plugins {
    alias(libs.plugins.kyoku.kmp.feature.ui)
}

kotlin {
    androidLibrary {
        namespace = "com.poulastaa.auth.ui"
    }
    sourceSets {
        commonMain.dependencies {
            implementation(projects.auth.domain)
        }
    }
}
