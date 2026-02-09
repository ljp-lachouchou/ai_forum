plugins {
    alias(libs.plugins.aiforum.android.feature.impl)
    alias(libs.plugins.aiforum.android.library.compose)
}

android {
    namespace = "feature.ljp.home.impl"
    testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
    implementation(libs.accompanist.permissions)
    implementation(libs.androidx.activity.compose)
    implementation(projects.core.domain)//
    implementation(projects.feature.home.api)
    implementation(projects.feature.profile.api)
    implementation(projects.feature.post.api)
    api(projects.sync)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}