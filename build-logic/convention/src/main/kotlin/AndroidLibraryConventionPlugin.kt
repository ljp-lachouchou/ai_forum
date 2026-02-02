import ai.ljp.convention.configureGradleManagedDevices
import ai.ljp.convention.configureKotlinAndroid
import ai.ljp.convention.libs
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.android.library")
            apply(plugin = "org.jetbrains.kotlin.android")
            apply(plugin = "aiforum.android.lint")
            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                testOptions.targetSdk = 36
                lint.targetSdk = 36
                defaultConfig.targetSdk = 36
                defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                testOptions.animationsDisabled = true
                configureGradleManagedDevices(this)
                //path ->:core:common
                //\W = 非字母数字字符
                resourcePrefix = path
                    .split(""""\w""".toRegex()).drop(1).distinct().joinToString(separator = "_")
                    .lowercase() + "_"
            }
            dependencies {
                "implementation"(libs.findLibrary("androidx.lifecycle.viewmodel.ktx").get())
                "implementation"(libs.findLibrary("androidx.lifecycle.runtime.ktx").get())
                // Default test libraries for JVM and Android tests.
                "androidTestImplementation"(libs.findLibrary("kotlin.test").get())
                "testImplementation"(libs.findLibrary("kotlin.test").get())

                // Tracing is commonly used across core libraries.
                "implementation"(libs.findLibrary("androidx.tracing.ktx").get())
            }
        }
    }
}