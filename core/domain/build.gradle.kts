plugins {
    alias(libs.plugins.aiforum.android.library)
    alias(libs.plugins.aiforum.hilt)
    alias(libs.plugins.aiforum.android.paging)
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