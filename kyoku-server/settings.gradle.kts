pluginManagement {
    includeBuild("build-logic")

    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven { url = uri("https://packages.confluent.io/maven/") }
    }
}


rootProject.name = "kyoku-server"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include("core:domain")
project(":core:domain").name = "core-domain"
include("core:database")
project(":core:database").name = "core-database"
include("core:data")
project(":core:data").name = "core-data"
include("core:network")
findProject(":core:network")?.name = "core-network"

include("user:data")
project(":user:data").name = "user-data"
include("user:domain")
project(":user:domain").name = "user-domain"
include("user:network")
findProject(":user:network")?.name = "user-network"

include("search:domain")
project(":search:domain").name = "search-domain"
include("search:data")
project(":search:data").name = "search-data"

include("auth:domain")
project(":auth:domain").name = "auth-domain"
include("auth:data")
project(":auth:data").name = "auth-data"
include("auth:network")
project(":auth:network").name = "auth-network"

include("details:domain")
project(":details:domain").name = "details-domain"
include("details:data")
project(":details:data").name = "details-data"

include("play:domain")
project(":play:domain").name = "play-domain"
include("play:data")
project(":play:data").name = "play-data"

include("suggestion:domain")
project(":suggestion:domain").name = "suggestion-domain"
include("suggestion:data")
project(":suggestion:data").name = "suggestion-data"

include("app")
project(":app").name = "app"

include("notification:domain")
project(":notification:domain").name = "notification-domain"
include("notification:data")
project(":notification:data").name = "notification-data"
