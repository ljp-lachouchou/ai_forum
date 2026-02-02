import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.aiforum.android.library)
    alias(libs.plugins.aiforum.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "io.ljp.simapi"

    buildFeatures {
        buildConfig = true
    }
    testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
    api(libs.kotlinx.datetime)
// Top-level build file where you can add configuration options common to all sub-projects/modules.
    implementation(libs.slf4j.android)//
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.websockets)
    implementation(libs.ktor.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.serialization.kotlinx.xml)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.ktor.logging)
    implementation(projects.core.common)

    implementation(libs.coil.kt)
    implementation(libs.coil.kt.svg)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
}