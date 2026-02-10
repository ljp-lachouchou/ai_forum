plugins {
    alias(libs.plugins.aiforum.android.feature.impl)
    alias(libs.plugins.aiforum.android.library.compose)
}

android {
    namespace = "feature.ljp.me.impl"

}

dependencies {

    implementation(projects.core.domain)//
    implementation(projects.feature.me.api)//
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}