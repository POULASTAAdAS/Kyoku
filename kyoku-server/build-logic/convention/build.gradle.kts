plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

group = "com.poulastaa.kyoku.buildlogic"

dependencies {
    compileOnly(libs.kotlin.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("jvmLibrary") {
            id = "kyoku.jvm.library"
            implementationClass = "JVMLibraryConventionPlugin"
        }

        register("ktorApplication") {
            id = "kyoku.ktor.application"
            implementationClass = "KtorConventionPlugin"
        }

        register("ktorExposed") {
            id = "kyoku.ktor.exposed"
            implementationClass = "ExposedConventionPlugin"
        }

        register("ktorLibrary") {
            id = "kyoku.ktor.library"
            implementationClass = "KtorLibraryConventionPlugin"
        }

        register("ktorKoin") {
            id = "kyoku.ktor.koin"
            implementationClass = "KoinConventionPlugin"
        }
    }
}