plugins {
    alias(libs.plugins.kyoku.kmp.library)
}

kotlin {
    androidLibrary {
        namespace = "com.poulastaa.auth.domain"
    }
}
