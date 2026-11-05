plugins {
    alias(libs.plugins.kyoku.kmp.library)
    alias(libs.plugins.kyoku.kmp.koin)
}

kotlin {
    androidLibrary {
        namespace = "com.poulastaa.auth.data"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.auth.domain)
            implementation(projects.common.network)
            implementation(projects.common.database)
        }
    }
}
