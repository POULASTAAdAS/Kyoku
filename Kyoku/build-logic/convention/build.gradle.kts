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
        register("androidApplication"){
            id = "kyoku.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
    }

    plugins {
        register("androidApplicationCompose"){
            id = "kyoku.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
    }

    plugins {
        register("androidLibrary"){
            id = "kyoku.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
    }

    plugins {
        register("androidLibraryCompose"){
            id = "kyoku.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
    }
}