plugins {
    `kotlin-dsl`
}

group = "com.poulastaa.kyoku.buildlogic"

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("kmpLibrary") {
            id = "kyoku.kmp.library"
            implementationClass = "KmpLibraryConventionPlugin"
        }
        register("kmpComposeLibrary") {
            id = "kyoku.kmp.compose.library"
            implementationClass = "KmpComposeLibraryConventionPlugin"
        }
        register("kmpFeatureUi") {
            id = "kyoku.kmp.feature.ui"
            implementationClass = "KmpFeatureUiConventionPlugin"
        }
        register("kmpCommonUi") {
            id = "kyoku.kmp.common.ui"
            implementationClass = "KmpCommonUiConventionPlugin"
        }
        register("kmpApplication") {
            id = "kyoku.kmp.application"
            implementationClass = "KmpApplicationConventionPlugin"
        }
        register("kmpNetwork") {
            id = "kyoku.kmp.network"
            implementationClass = "KmpNetworkConventionPlugin"
        }
    }
}
