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
