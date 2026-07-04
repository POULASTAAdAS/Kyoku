plugins {
    alias(libs.plugins.kyoku.kmp.network)
}

kotlin {
    androidLibrary {
        namespace = "com.poulastaa.common.network"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.common.domain)
        }
    }
}
