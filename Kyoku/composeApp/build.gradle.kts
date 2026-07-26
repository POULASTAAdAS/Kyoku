import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kyoku.kmp.application)
}

android {
    namespace = "com.poulastaa.kyoku"

    defaultConfig {
        applicationId = "com.poulastaa.kyoku"
        //noinspection OldTargetApi
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.auth.data)
            implementation(projects.auth.domain)
            implementation(projects.auth.network)
            api(projects.auth.ui)

            implementation(projects.common.domain)
            implementation(projects.common.ui)

            implementation(projects.setup.domain)
            implementation(projects.setup.ui)

            implementation(libs.ktor.client.core)
        }
        androidMain.dependencies {
            implementation(libs.androidx.splashscreen)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
        }
    }
}

compose.desktop {
    application {
        mainClass = "com.poulastaa.kyoku.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.poulastaa.kyoku"
            packageVersion = "1.0.0"

            val iconsRoot = project.file("src/jvmMain/resources")
            macOS {
                iconFile.set(iconsRoot.resolve("app-icon.icns"))
            }
            windows {
                iconFile.set(iconsRoot.resolve("app-icon.ico"))
            }
            linux {
                iconFile.set(iconsRoot.resolve("app-icon.png"))
            }
        }
    }
}
