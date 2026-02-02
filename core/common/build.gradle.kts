plugins {
    alias(libs.plugins.aiforum.jvm.library)
    alias(libs.plugins.aiforum.hilt)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
}