plugins {
    alias(libs.plugins.kyoku.kmp.room)
}

android {
    namespace = "com.poulastaa.common.database"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.androidx.room.runtime)
        }
    }
}
