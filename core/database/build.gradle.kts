plugins {
    alias(libs.plugins.aiforum.android.library)
    alias(libs.plugins.aiforum.android.room)
    alias(libs.plugins.aiforum.hilt)
    alias(libs.plugins.aiforum.android.paging)
}

android {
    namespace = "ai.ljp.database"

}

dependencies {
    api(projects.core.model)

    implementation(libs.kotlinx.datetime)

    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.kotlinx.coroutines.test)
}