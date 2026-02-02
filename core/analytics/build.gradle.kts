plugins {
    alias(libs.plugins.aiforum.android.library)
    alias(libs.plugins.aiforum.hilt)
}

android {
    namespace = "ai.ljp.analytics"

}

dependencies {

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
}