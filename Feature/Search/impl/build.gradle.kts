plugins {
    alias(libs.plugins.aiforum.android.feature.impl)
    alias(libs.plugins.aiforum.android.library.compose)
}

android {
    namespace = "feature.ljp.search.impl"

}

dependencies {

    implementation(projects.core.domain)//
    implementation(projects.feature.search.api)
    implementation(projects.feature.post.api)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}