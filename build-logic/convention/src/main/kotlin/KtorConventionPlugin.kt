import ai.ljp.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class KtorConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            dependencies {
                "implementation"(libs.findLibrary("ktor.client.core").get())
                "implementation"(libs.findLibrary("ktor.client.cio").get())
                "implementation"(libs.findLibrary("ktor.client.okhttp").get())
                "implementation"(libs.findLibrary("ktor.client.websockets").get())
                "implementation"(libs.findLibrary("ktor.client.auth").get())
                "implementation"(libs.findLibrary("ktor.content.negotiation").get())
                "implementation"(libs.findLibrary("ktor.serialization.kotlinx.json").get())
                "implementation"(libs.findLibrary("ktor.serialization.kotlinx.xml").get())
                "implementation"(libs.findLibrary("kotlinx.serialization.json").get())
                "implementation"(libs.findLibrary("ktor.logging").get())
            }
        }
    }
}