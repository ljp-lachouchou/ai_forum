plugins {
    alias(libs.plugins.aiforum.android.library)
    alias(libs.plugins.aiforum.android.library.compose)
    alias(libs.plugins.aiforum.hilt)
    alias(libs.plugins.roborazzi)
    alias(libs.plugins.aiforum.ktor)
}

android {
    namespace = "ai.ljp.designsystem"
    testOptions.unitTests.isIncludeAndroidResources = true

}

dependencies {

    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.foundation.layout)
    api(libs.androidx.compose.material.iconsExtended)
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.material3.adaptive)
    api(libs.androidx.compose.material3.navigationSuite)
    api(libs.androidx.compose.runtime)
    api(libs.androidx.compose.ui.util)
    api(libs.commonmark.core)
    api(libs.commonmark.ext.table)
    api(libs.commonmark.ext.heading)
    api(libs.commonmark.ext.task)

    implementation(libs.coil.kt.compose)
    implementation(libs.androidx.compose.animation)

    testImplementation(libs.androidx.compose.ui.test)
    testImplementation(libs.androidx.compose.ui.testManifest)

    testImplementation(libs.hilt.android.testing)
    testImplementation(libs.robolectric)
}