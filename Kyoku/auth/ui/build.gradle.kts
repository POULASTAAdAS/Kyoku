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

        androidMain.dependencies {
            implementation(libs.androidx.credentials)
            implementation(libs.androidx.credentials.play.services.auth)
            implementation(libs.googleid)
        }
    }
}
