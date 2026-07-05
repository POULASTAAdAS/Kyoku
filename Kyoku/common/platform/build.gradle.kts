plugins {
    alias(libs.plugins.kyoku.kmp.library)
}

kotlin {
    androidLibrary {
        namespace = "com.poulastaa.common.platform"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.common.domain)
        }
    }
}
