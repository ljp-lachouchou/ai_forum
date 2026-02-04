plugins {
    alias(libs.plugins.aiforum.android.library)
    alias(libs.plugins.aiforum.hilt)
}

android {
    namespace = "ai.ljp.notification"


}

dependencies {

    api(projects.core.model)

    implementation(projects.core.common)


}