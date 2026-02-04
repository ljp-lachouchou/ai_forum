plugins {
    alias(libs.plugins.aiforum.android.library)
    alias(libs.plugins.aiforum.hilt)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.aiforum.android.paging)
}

android {
    namespace = "ai.ljp.data"
    testOptions.unitTests.isIncludeAndroidResources = true

}

dependencies {

    api(projects.core.common)
    api(projects.core.database)
    api(projects.core.datastore)
    api(projects.core.network)

    implementation(projects.core.analytics)
    testImplementation(libs.kotlinx.serialization.json)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}