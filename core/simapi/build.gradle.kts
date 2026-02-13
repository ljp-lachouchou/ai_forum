import com.android.build.api.variant.BuildConfigField
import java.io.StringReader
import java.util.Properties

plugins {
    alias(libs.plugins.aiforum.android.library)
    alias(libs.plugins.aiforum.hilt)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.aiforum.ktor)
}

android {
    namespace = "io.ljp.simapi"

    buildFeatures {
        buildConfig = true
    }
    testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
    api(libs.kotlinx.datetime)
// Top-level build file where you can add configuration options common to all sub-projects/modules.
    implementation(libs.slf4j.android)//
    implementation(libs.ktor.client.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(projects.core.common)
    implementation(projects.core.datastore)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
}
val baseUrl = providers.fileContents(
    isolated.rootProject.projectDirectory.file("local.properties")
).asText.map { text ->
    val properties = Properties()
    properties.load(StringReader(text))
    properties["BACKEND_URL"]
}.orElse("http://115.190.188.164:9000")

androidComponents {
    onVariants {
        it.buildConfigFields!!.put("BACKEND_URL",
            baseUrl.map { value ->
                BuildConfigField(
                    type = "String",
                    value = """"$value"""",//保证有""
                    comment = null
                )
            }
        )
    }
}