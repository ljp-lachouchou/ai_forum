import org.gradle.kotlin.dsl.implementation

plugins {
    alias(libs.plugins.aiforum.android.application)
    alias(libs.plugins.aiforum.android.application.compose)
    alias(libs.plugins.aiforum.hilt)
//    alias(libs.plugins.aiforum.android.application.firebase)
    alias(libs.plugins.google.osslicenses)
    alias(libs.plugins.baselineprofile)
    alias(libs.plugins.roborazzi)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "ai.ljp.aiforum"

    defaultConfig {
        applicationId = "ai.ljp.aiforum"
        versionCode = 1
        versionName = "1.0"
        manifestPlaceholders += mapOf(
            "JPUSH_PKGNAME" to applicationId!!,
            "JPUSH_APPKEY" to "6778f4b44dcfcec3b40be16e", // 填入极光后台获取的 24 位 AppKey
            "JPUSH_CHANNEL" to "developer-default"
        )

        // 建议保留 ndk 配置，确保在 64 位手机上不崩溃
        ndk {
            abiFilters += listOf("armeabi-v7a", "arm64-v8a", "x86_64")
        }
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = providers.gradleProperty("minifyWithR8")
                .map(String::toBooleanStrict).getOrElse(true)
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.named("debug").get()
        }
    }
    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
    testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {

    implementation(projects.feature.home.api)
    implementation(projects.feature.home.impl)
    implementation(projects.feature.post.api)
    implementation(projects.feature.post.impl)
    implementation(projects.feature.profile.api)
    implementation(projects.feature.profile.impl)
    implementation(projects.feature.login.api)
    implementation(projects.feature.login.impl)
    implementation(projects.feature.search.api)
    implementation(projects.feature.search.impl)
    implementation(projects.feature.me.api)
    implementation(projects.feature.me.impl)
    implementation(projects.feature.treehole.api)
    implementation(projects.feature.treehole.impl)
    implementation(projects.feature.treeholeCreate.api)
    implementation(projects.feature.treeholeCreate.impl)
    implementation(projects.feature.postCreate.api)
    implementation(projects.feature.postCreate.impl)



    implementation(projects.core.common)
    implementation(projects.core.ui)
    implementation(projects.core.designsystem)
    implementation(projects.core.data)
    implementation(projects.core.model)
    implementation(projects.core.analytics)
    implementation(projects.sync)

    implementation(libs.slf4j.android)

//    implementation(platform(libs.firebase.bom))
//    implementation(libs.firebase.cloud.messaging)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.compose.material3.adaptive)
    implementation(libs.androidx.compose.material3.adaptive.layout)
    implementation(libs.androidx.compose.material3.adaptive.navigation)
    implementation(libs.androidx.compose.material3.adaptive.navigation3)
    implementation(libs.androidx.compose.material3.windowSizeClass)
    implementation(libs.androidx.compose.runtime.tracing)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.lifecycle.viewModel.navigation3)
    implementation(libs.androidx.profileinstaller)
    implementation(libs.androidx.tracing.ktx)
    implementation(libs.androidx.window.core)
    implementation(libs.kotlinx.coroutines.guava)
    implementation(libs.coil.kt)
    implementation(libs.kotlinx.serialization.json)

    ksp(libs.hilt.compiler)

    debugImplementation(libs.androidx.compose.ui.testManifest)

    kspTest(libs.hilt.compiler)

    testImplementation(libs.hilt.android.testing)
    testImplementation(libs.kotlin.test)

    testImplementation(libs.androidx.navigation.testing)
    testImplementation(libs.robolectric)
    testImplementation(libs.roborazzi)

    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test)
    androidTestImplementation(libs.hilt.android.testing)
    androidTestImplementation(libs.kotlin.test)



}