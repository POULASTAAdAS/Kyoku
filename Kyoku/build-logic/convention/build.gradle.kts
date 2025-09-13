plugins {
    `kotlin-dsl`
}

group = "com.poulastaa.kyoku.buildlogic"

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.room.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "kyoku.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }

        register("androidApplicationCompose") {
            id = "kyoku.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }

        register("androidLibrary") {
            id = "kyoku.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }

        register("androidLibraryCompose") {
            id = "kyoku.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }

        register("androidFeatureUi") {
            id = "kyoku.android.feature.ui"
            implementationClass = "AndroidFeatureUiConventionPlugin"
        }

        register("androidRoom") {
            id = "kyoku.android.room"
            implementationClass = "AndroidRoomConventionPlugin"
        }

        register("androidDagger") {
            id = "kyoku.android.dagger"
            implementationClass = "AndroidDaggerHiltConventionPlugin"
        }

        register("jvmLibrary") {
            id = "kyoku.jvm.library"
            implementationClass = "JVMLibraryConventionPlugin"
        }

        register("OkhttpLibrary") {
            id = "kyoku.okhttp.library"
            implementationClass = "OkHttpConventionPlugin"
        }
    }
}
