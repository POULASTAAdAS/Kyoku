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
findProject(":core:domain")?.name = "domain"
include("core:database")
findProject(":core:database")?.name = "database"
include("core:data")
findProject(":core:data")?.name = "data"

include("user:data")
findProject(":user:data")?.name = "data"
include("user:domain")
findProject(":user:domain")?.name = "domain"

include("search:domain")
findProject(":search:domain")?.name = "domain"
include("search:data")
findProject(":search:data")?.name = "data"

include("auth:domain")
findProject(":auth:domain")?.name = "domain"
include("auth:data")
findProject(":auth:data")?.name = "data"

include("details:domain")
findProject(":details:domain")?.name = "domain"
include("details:data")
findProject(":details:data")?.name = "data"

include("play:domain")
findProject(":play:domain")?.name = "domain"
include("play:data")
findProject(":play:data")?.name = "data"

include("suggestion:domain")
findProject(":suggestion:domain")?.name = "domain"
include("suggestion:data")
findProject(":suggestion:data")?.name = "data"