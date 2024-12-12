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
include(":app")

include(":core:domain")
include(":core:data")
include(":core:database")
include(":core:presentation:ui")
include(":core:presentation:designsystem")

include(":auth:domain")
include(":auth:data")
include(":auth:presentation")

include(":player:domain")
include(":player:data")
include(":player:presentation")

include(":settings:domain")
include(":settings:data")
include(":settings:presentation")

include(":profile:domain")
include(":profile:data")
include(":profile:presentation")

include(":setup:domain")
include(":setup:data")
include(":setup:presentation")

include(":history:domain")
include(":history:data")
include(":history:presentation")

include(":details:domain")
include(":details:data")
include(":details:presentation")

include(":playlist:domain")
include(":playlist:data")
include(":playlist:presentation")
