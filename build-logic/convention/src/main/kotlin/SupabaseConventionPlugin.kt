import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class SupabaseConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            dependencies {
                "implementation"(platform("io.github.jan-tennert.supabase:bom:3.0.0"))

                // 存储服务模块 (Storage)
                "implementation"("io.github.jan-tennert.supabase:storage-kt")
            }
        }
    }
}