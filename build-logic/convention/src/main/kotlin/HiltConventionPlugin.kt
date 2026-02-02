import ai.ljp.convention.libs
import com.android.build.gradle.api.AndroidBasePlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import kotlin.text.get

class HiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.google.devtools.ksp")
            dependencies {
                // KSP processor for Hilt.
                "ksp"(libs.findLibrary("hilt.compiler").get())
            }
            //这是一个 jvm/kotlin module 添加hilt
            pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
                dependencies {
                    // Hilt core for pure JVM modules.
                    "implementation"(libs.findLibrary("hilt.core").get())
                }
            }

            /** 这是一个 Android module 添加hilt*/
            pluginManager.withPlugin("com.android.base") {
                apply(plugin = "dagger.hilt.android.plugin")
                dependencies {
                    "implementation"(libs.findLibrary("hilt.android").get())
                }
            }
        }
    }
}