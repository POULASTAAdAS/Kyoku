plugins {
    alias(libs.plugins.kyoku.kmp.common.ui)
    alias(libs.plugins.kotlinSerializationPlugin)
}

kotlin {
    androidLibrary {
        namespace = "com.poulastaa.common.ui"
    }
}
