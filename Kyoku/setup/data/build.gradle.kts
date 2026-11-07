plugins {
    alias(libs.plugins.kyoku.kmp.library)
    alias(libs.plugins.kyoku.kmp.koin)
}

kotlin {
    androidLibrary {
        namespace = "com.poulastaa.setup.data"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.setup.domain)
            implementation(projects.common.database)
        }
    }
}
