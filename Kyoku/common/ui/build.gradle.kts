plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.kotlinSerializationPlugin)
}

kotlin {
    androidTarget()

    val xcfName = "common-uiKit"

    iosX64 {
        binaries.framework { baseName = xcfName }
    }
    iosArm64 {
        binaries.framework { baseName = xcfName }
    }
    iosSimulatorArm64 {
        binaries.framework { baseName = xcfName }
    }

    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.ui.backhandler)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.material3.window.size.classss)
            implementation(libs.androidx.navigation)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(projects.common.domain)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

dependencies {
    debugImplementation(libs.androidx.compose.uiTooling)
}

android {
    namespace = "com.poulastaa.common.ui"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    sourceSets {
        getByName("main") {
            res.srcDirs("src/androidMain/res")
        }
    }
}
