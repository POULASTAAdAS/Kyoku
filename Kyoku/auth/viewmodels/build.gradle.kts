plugins {
    alias(libs.plugins.kyoku.kmp.viewmodels)
}

android {
    namespace = "com.poulastaa.auth.viewmodels"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.common.viewmodels)
            implementation(projects.auth.domain)
        }
    }
}
