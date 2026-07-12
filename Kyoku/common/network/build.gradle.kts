plugins {
    alias(libs.plugins.kyoku.kmp.network)
}

kotlin {
    androidLibrary {
        namespace = "com.poulastaa.common.network"
    }

    sourceSets {
        commonMain.dependencies {
            api(projects.common.domain)
            implementation(projects.common.platform)
        }
    }
}
