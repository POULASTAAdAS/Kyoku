plugins {
    alias(libs.plugins.kyoku.kmp.common.ui)
    alias(libs.plugins.kotlinSerializationPlugin)
}

android {
    namespace = "com.poulastaa.common.ui"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.common.viewmodels)

            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)
        }
        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        jvmMain.dependencies {
            implementation(libs.ktor.client.cio)
        }
    }
}
