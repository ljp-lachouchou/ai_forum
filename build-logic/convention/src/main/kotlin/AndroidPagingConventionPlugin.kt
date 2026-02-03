import ai.ljp.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidPagingConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            dependencies {
                "implementation"(libs.findLibrary("androidx.paging.runtime.ktx").get())
                "implementation"(libs.findLibrary("androidx.paging.compose").get())
                "implementation"(libs.findLibrary("androidx.room.paging").get())
            }
        }
    }

}