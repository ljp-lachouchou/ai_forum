import ai.ljp.convention.configureGradleManagedDevices
import ai.ljp.convention.configureKotlinAndroid
import ai.ljp.convention.libs
import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

/**
 *android应用基础插件
 */
class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.android.application")
            apply(plugin = "org.jetbrains.kotlin.android")
            apply(plugin = "aiforum.android.lint")


            //用“依赖快照/锁定”防止你的模块依赖在不知不觉中变多、变错或发生漂移（dependency creep），并让依赖变化必须显式审核。
            // Dependency-guard enforces a stable dependency graph.
            apply(plugin = "com.dropbox.dependency-guard")
            extensions.configure<ApplicationExtension> {

                defaultConfig.targetSdk = 36
                configureKotlinAndroid(this)
                testOptions.animationsDisabled = true
                configureGradleManagedDevices(this)
            }
            extensions.configure<ApplicationAndroidComponentsExtension> {
                //TODO: 打印androidTest构建出来的apk路径
                //TODO：打印apk报告任务
            }
            dependencies {
                "implementation"(libs.findLibrary("androidx.lifecycle.viewmodel.ktx").get())
                "implementation"(libs.findLibrary("androidx.lifecycle.runtime.ktx").get())
            }
        }
    }
}