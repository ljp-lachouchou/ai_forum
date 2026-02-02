import ai.ljp.convention.libs
import com.android.build.api.dsl.ApplicationExtension
import com.google.firebase.crashlytics.buildtools.gradle.CrashlyticsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.exclude
//firebase插件配置
class AndroidApplicationFirebaseConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.google.gms.google-services")
            apply(plugin = "com.google.firebase.firebase-perf")
            apply(plugin = "com.google.firebase.crashlytics")
            dependencies {
                val bom = libs.findLibrary("firebase-bom").get()
                "implementation"(platform(bom))
                "implementation"(libs.findLibrary("firebase.analytics").get())
                "implementation"(libs.findLibrary("firebase.performance").get()) {
                    /*
                    排除protobuf相关依赖，因为需要在项目中其他地方使用protobuf相关。防止protobuf相关库冲突。
                    */
                    exclude(group = "com.google.protobuf", module = "protobuf-javalite")
                    exclude(group = "com.google.firebase", module = "protolite-well-known-types")
                }
                "implementation"(libs.findLibrary("firebase.crashlytics").get())
            }
            //在release的环境下，才开启混淆映射文件的上传
            extensions.configure<ApplicationExtension> {
                buildTypes {
                    release {
                        configure<CrashlyticsExtension> {
                            mappingFileUploadEnabled = true
                        }
                    }
                    debug {
                        // debug 通常不上报 mapping（也不一定启用 minify）
                        configure<CrashlyticsExtension> {
                            mappingFileUploadEnabled = false
                        }
                    }

                }
            }

        }
    }
}