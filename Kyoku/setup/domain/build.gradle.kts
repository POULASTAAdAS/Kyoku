plugins {
    alias(libs.plugins.kyoku.kmp.domain)
}

kotlin {
    androidLibrary {
        namespace = "com.poulastaa.setup.domain"
    }
    sourceSets {
        commonMain.dependencies {
            api(projects.common.domain)
        }
    }
}
