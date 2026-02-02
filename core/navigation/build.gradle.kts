plugins {
    alias(libs.plugins.aiforum.android.library)
    alias(libs.plugins.aiforum.hilt)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose)
}

android {
    namespace = "ai.ljp.navigation"

}

dependencies {

    api(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.savedstate.compose)
    implementation(libs.androidx.lifecycle.viewModel.navigation3)

    testImplementation(libs.truth)

    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.compose.ui.testManifest)
    androidTestImplementation(libs.androidx.lifecycle.viewModel.testing)
    androidTestImplementation(libs.truth)
}