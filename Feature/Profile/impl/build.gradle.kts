plugins {
    alias(libs.plugins.aiforum.android.feature.impl)
    alias(libs.plugins.aiforum.android.library.compose)
}

android {
    namespace = "featurei.ljp.profile.impl"

}

dependencies {

    implementation(projects.core.domain)//
    implementation(projects.feature.profile.api)
    implementation(projects.feature.post.api)
    implementation(libs.androidx.compose.material3.adaptive.navigation3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}