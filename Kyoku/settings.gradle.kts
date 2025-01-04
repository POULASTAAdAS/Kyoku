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

rootProject.name = "Kyoku"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")


include(":app")
include(":core:domain")
include(":core:data")
include(":core:database")
include(":core:presentation:ui")
include(":core:presentation:designsystem")
include(":auth:domain")
include(":auth:data")
include(":auth:presentation")
include(":auth:network")
include(":settings:domain")
include(":settings:data")
include(":settings:presentation")
include(":settings:network")
include(":setup:domain")
include(":setup:data")
include(":setup:presentation")
include(":setup:network")
