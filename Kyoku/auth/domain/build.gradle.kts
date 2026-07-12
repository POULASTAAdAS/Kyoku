plugins {
    alias(libs.plugins.kyoku.kmp.domain)
}

kotlin {
    androidLibrary {
        namespace = "com.poulastaa.auth.domain"
    }
    sourceSets {
        commonMain.dependencies {
            api(projects.common.domain)
        }
    }
}
