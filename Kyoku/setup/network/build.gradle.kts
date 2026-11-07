plugins {
    alias(libs.plugins.kyoku.kmp.network)
}

kotlin {
    androidLibrary {
        namespace = "com.poulastaa.setup.network"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.setup.domain)
            implementation(libs.ktor.client.core)
        }
    }
}
