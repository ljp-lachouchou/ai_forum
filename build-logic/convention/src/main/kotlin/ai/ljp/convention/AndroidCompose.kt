package ai.ljp.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension


internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*,*,*,*,*,*>
) {
    commonExtension.apply {
        buildFeatures {
            compose = true
        }
        dependencies {
            // 使用Compose BOM来保持Compose库的对齐。
            val bom = libs.findLibrary("androidx-compose-bom").get()
            "implementation"(platform(bom))
            "androidTestImplementation"(platform(bom))
            "implementation"(libs.findLibrary("androidx-compose-ui-tooling-preview").get())
            "debugImplementation"(libs.findLibrary("androidx-compose-ui-tooling").get())
        }

    }
    extensions.configure<ComposeCompilerGradlePluginExtension> {//检查compose重组是否稳定
        // 仅在gradle属性为真时启用metrics/reports的助手。
        fun Provider<String>.onlyIfTrue() = flatMap { provider { it.takeIf(String::toBoolean) } }
        // 在根项目的build文件夹中解析一个目录路径。
        fun Provider<*>.relativeToRootProject(dir: String) = map {
            isolated.rootProject.projectDirectory
                .dir("build") // 根目录build
                .dir(projectDir.toRelativeString(rootDir)) // 根据当前模块的位置生成路径
        }.map { it.dir(dir) }

        // 可选的指标和报告目录（通过gradle.properties启用）。
        project.providers.gradleProperty("enableComposeCompilerMetrics").onlyIfTrue()
            .relativeToRootProject("compose-metrics")
            .let(metricsDestination::set)

        project.providers.gradleProperty("enableComposeCompilerReports").onlyIfTrue()
            .relativeToRootProject("compose-reports")
            .let(reportsDestination::set)

        // 将此文件视为 Compose 编译器稳定性的真实来源。
        stabilityConfigurationFiles
            .add(isolated.rootProject.projectDirectory.file("compose_compiler_config.conf"))
    }
}