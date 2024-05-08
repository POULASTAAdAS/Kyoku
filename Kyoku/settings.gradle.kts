pluginManagement {
    includeBuild("build-logic")

    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "Kyoku"
include(":app")
include(":core:domain")
include(":core:presentation:ui")
include(":core:presentation:designsystem")
include(":core:data")
include(":core:database")
include(":auth:data")
include(":auth:presentation")
include(":auth:domain")
include(":play:data")
include(":play:domain")
include(":play:presentation")
include(":play:network")
