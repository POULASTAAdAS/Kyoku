plugins {
    alias(libs.plugins.kyoku.kmp.room)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.auth.domain)
        }
    }
}

android {
    namespace = "com.poulastaa.auth.data"
}
