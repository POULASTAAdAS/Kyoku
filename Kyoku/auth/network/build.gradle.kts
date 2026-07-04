plugins {
    alias(libs.plugins.kyoku.kmp.network)
}

kotlin {
    androidLibrary {
        namespace = "com.poulastaa.auth.network"
    }
}
