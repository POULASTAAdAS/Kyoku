plugins {
    alias(libs.plugins.kyoku.kmp.viewmodels)
}

android {
    namespace = "com.poulastaa.setup.viewmodels"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.common.viewmodels)
            implementation(projects.setup.domain)
        }
    }
}
