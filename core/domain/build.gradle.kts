plugins {
    alias(libs.plugins.aiforum.android.library)
    alias(libs.plugins.aiforum.hilt)
}

android {
    namespace = "ai.ljp.domain"

}

dependencies {

    api(projects.core.data)
    api(projects.core.model)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}