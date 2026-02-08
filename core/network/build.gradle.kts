import com.android.build.api.variant.BuildConfigField
import java.io.StringReader
import java.util.Properties

plugins {
    alias(libs.plugins.aiforum.android.library)
    alias(libs.plugins.aiforum.hilt)
    alias(libs.plugins.aiforum.ktor)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.aiforum.supabase)
}

android {
    namespace = "ai.ljp.network"
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    api(libs.kotlinx.datetime)
    api(projects.core.common)
    api(projects.core.model)
    api(projects.core.simapi)

    implementation(libs.coil.kt)
    implementation(libs.coil.kt.svg)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
val supabaseUrl = providers.fileContents(
    isolated.rootProject.projectDirectory.file("local.properties")
).asText.map { text ->
    val properties = Properties()
    properties.load(StringReader(text))
    properties["SUPABASE_URL"]
}.orElse("https://example.supabase.co")
val supabaseKey = providers.fileContents(
    isolated.rootProject.projectDirectory.file("local.properties")
).asText.map { text ->
    val properties = Properties()
    properties.load(StringReader(text))
    properties["SUPABASE_KEY"]
}.orElse("key")
androidComponents {
    onVariants {
        it.buildConfigFields!!.apply {
            put("SUPABASE_URL",
            supabaseUrl.map { value ->
                BuildConfigField(
                    type = "String",
                    value = """"$value"""",//保证有""
                    comment = null
                )
            }
            )
            put(
                "SUPABASE_KEY",
                supabaseKey.map { value ->
                    BuildConfigField(
                        type = "String",
                        value = """"$value"""",//保证有""
                        comment = null
                    )
                }
            )
        }
    }
}