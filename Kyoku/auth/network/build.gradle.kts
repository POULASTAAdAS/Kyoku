plugins {
    alias(libs.plugins.kyoku.kmp.network)
}

kotlin {
    androidLibrary {
        namespace = "com.poulastaa.auth.network"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.auth.domain)
            implementation(projects.common.platform)
        }
    }
}
