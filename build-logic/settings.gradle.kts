pluginManagement {
    repositories {
        // Plugin repositories for the build-logic included build.
        gradlePluginPortal()
        google()
    }
}

dependencyResolutionManagement {
    repositories {
        // Keep repository ordering explicit and scoped to Google/Android artifacts first.
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        // Everything else comes from Maven Central.
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            // Reuse the root project's version catalog for consistency.
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

// Give the included build a stable name and include the convention module.
rootProject.name = "build-logic"
include(":convention")