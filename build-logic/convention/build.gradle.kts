import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
    alias(libs.plugins.android.lint)
}

group = "ai.ljp.convention"

// Configure the build-logic plugins to target JDK 17.
// This matches the JDK used to build the project, and is not related to what runs on devices.
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        // Align Kotlin compiler bytecode target with the Java toolchain.
        jvmTarget = JvmTarget.JVM_17
    }
}

dependencies {
    // compileOnly keeps plugin compile-time APIs on the classpath without bundling them into the jar.
    compileOnly(libs.android.gradleApiPlugin) // 告诉编译器：“我写插件代码时需要引用这些类的 API，但我不负责把这些类打包进去
    compileOnly(libs.android.tools.common)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.firebase.crashlytics.gradlePlugin) // 你的插件在运行时确实需要这个库的代码，而且 Gradle 环境里不提供它。
    compileOnly(libs.firebase.performance.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.room.gradlePlugin)
    // Truth is used in custom tasks/tests for readable assertions.
    implementation(libs.truth)
    // Custom lint checks used by the convention plugins.
    lintChecks(libs.androidx.lint.gradle)
}

tasks {
    validatePlugins {
        // Fail fast on invalid metadata so plugin IDs stay trustworthy.
        enableStricterValidation = true
        failOnWarning = true
    }
}
gradlePlugin {
    plugins {
        // Register each convention plugin with a stable ID and implementation class.
        register("androidApplicationCompose") {
            id = libs.plugins.aiforum.android.application.compose.get().pluginId
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("androidApplication") {
            id = libs.plugins.aiforum.android.application.asProvider().get().pluginId
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = libs.plugins.aiforum.android.library.compose.get().pluginId
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("androidLibrary") {
            id = libs.plugins.aiforum.android.library.asProvider().get().pluginId
            implementationClass = "AndroidLibraryConventionPlugin"//
        }
        register("hilt") {
            id = libs.plugins.aiforum.hilt.get().pluginId
            implementationClass = "HiltConventionPlugin"
        }
        register("ktor") {
            id = libs.plugins.aiforum.ktor.get().pluginId
            implementationClass = "KtorConventionPlugin"
        }
        register("featureImpl") {
            id = libs.plugins.aiforum.android.feature.impl.get().pluginId
            implementationClass = "AndroidFeatureImplConventionPlugin"
        }
        register("featureApi") {
            id = libs.plugins.aiforum.android.feature.api.get().pluginId
            implementationClass = "AndroidFeatureApiConventionPlugin"
        }
        register("supabase") {
            id = libs.plugins.aiforum.supabase.get().pluginId
            implementationClass = "SupabaseConventionPlugin"
        }
        register("androidRoom") {
            id = libs.plugins.aiforum.android.room.get().pluginId
            implementationClass = "AndroidRoomConventionPlugin"
        }
        register("androidPaging") {
            id = libs.plugins.aiforum.android.paging.get().pluginId
            implementationClass = "AndroidPagingConventionPlugin"
        }
        register("androidFirebase") {
            id = libs.plugins.aiforum.android.application.firebase.get().pluginId
            implementationClass = "AndroidApplicationFirebaseConventionPlugin"
        }
        register("androidLint") {
            id = libs.plugins.aiforum.android.lint.get().pluginId
            implementationClass = "AndroidLintConventionPlugin"
        }
        register("jvmLibrary") {
            id = libs.plugins.aiforum.jvm.library.get().pluginId
            implementationClass = "JvmLibraryConventionPlugin"
        }
        register("root") {
            id = libs.plugins.aiforum.root.get().pluginId
            implementationClass = "RootPlugin"
        }
    }
}
