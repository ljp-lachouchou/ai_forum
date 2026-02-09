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
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
    }
}
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
rootProject.name = "AIForum"
include(":app")
include(":core:simapi")
include(":core:common")

include(":core:model")
include(":core:datastore-proto")
include(":core:analytics")
include(":core:designsystem")
include(":core:datastore")
include(":core:logger")
include(":core:event")
include(":core:navigation")
include(":core:database")
include(":core:network")
include(":core:data")
include(":core:notification")
include(":sync")
include(":feature")
include(":feature:Home")
include(":feature:Community")
include(":feature:Treehole")
include(":feature:Me")
include(":feature:Profile")
include(":core:ui")
include(":feature:Home:api")
include(":feature:Home:impl")
include(":core:domain")
include(":feature:Profile:api")
include(":feature:Profile:impl")
