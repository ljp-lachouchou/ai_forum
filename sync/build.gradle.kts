plugins {
    alias(libs.plugins.aiforum.android.library)
    alias(libs.plugins.aiforum.hilt)
}

android {
    namespace = "ai.ljp.sync"
}

dependencies {

    ksp(libs.hilt.ext.compiler)
    implementation(libs.androidx.tracing.ktx)
    implementation(libs.androidx.work.ktx)
    implementation(libs.hilt.ext.work)
    implementation(projects.core.analytics)
//    implementation(libs.firebase.cloud.messaging)
    implementation(projects.core.data)
    implementation(projects.core.notification)
    api(libs.jiguang.push)
//    implementation(platform(libs.firebase.bom))

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}