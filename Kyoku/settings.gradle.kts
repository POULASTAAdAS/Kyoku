pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}


rootProject.name = "Kyoku"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(
    ":auth:domain",
    ":auth:ui",
    ":composeApp"
)
include(":common:ui")
include(":common:domain")
include(":setup:ui")
include(":setup:data")
include(":setup:domain")
include(":auth:network")
