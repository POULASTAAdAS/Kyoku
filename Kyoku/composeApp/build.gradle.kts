import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kyoku.kmp.application)
}

android {
    namespace = "com.poulastaa.kyoku"

    defaultConfig {
        applicationId = "com.poulastaa.kyoku"
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.auth.domain)
            implementation(projects.auth.ui)

            implementation(projects.common.domain)
            implementation(projects.common.ui)

            implementation(projects.setup.domain)
            implementation(projects.setup.ui)
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
        }
    }
}
