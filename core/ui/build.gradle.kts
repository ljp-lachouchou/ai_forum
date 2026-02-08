plugins {
    alias(libs.plugins.aiforum.android.library)
    alias(libs.plugins.aiforum.android.library.compose)
    alias(libs.plugins.aiforum.android.paging)
}

android {
    namespace = "ai.ljp.ui"
}

dependencies {

    api(libs.androidx.metrics)
    api(projects.core.analytics)
    api(projects.core.designsystem)
    api(projects.core.model)

    implementation(libs.coil.kt)
    implementation(libs.coil.kt.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}