package ai.ljp.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinBaseExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension<*,*,*,*,*,*>
) {
    commonExtension.apply {
        compileSdk = 36
        defaultConfig {
            minSdk = 24
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
            isCoreLibraryDesugaringEnabled = true //在低 Android 版本上，也能安全使用“较新的 Java / Kotlin 标准库 API
        }
    }
    configureKotlin<KotlinAndroidProjectExtension>()

    dependencies {
        // 保证低版本android也能使用高版本java/kotlin的api
        //lib.toml文件中的android-desugarJdkLibs
        "coreLibraryDesugaring"(libs.findLibrary("android.desugarJdkLibs").get())
    }

}
internal fun Project.configureKotlinJvm() {
    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    configureKotlin<KotlinJvmProjectExtension>()
}
private inline fun <reified T : KotlinBaseExtension> Project.configureKotlin() = configure<T> {
    // 在gradle.properties中可配置
    val warningsAsErrors = providers.gradleProperty("warningsAsErrors").map { it.toBoolean() }
        .orElse(false)
    //现在一般使用 compilerOptions,而不是kotlinExtension
    when(this) {
        is KotlinAndroidProjectExtension -> compilerOptions
        is KotlinJvmProjectExtension -> compilerOptions
        else ->TODO("不被支持的插件扩展 $this ${T::class}")
    }.apply {
        //锚定所有module的kotlin语言版本
        languageVersion.set(KotlinVersion.KOTLIN_2_2)
        coreLibrariesVersion = "2.2.21"
        allWarningsAsErrors.set(warningsAsErrors)
        jvmTarget.set(JvmTarget.JVM_11)
        freeCompilerArgs.addAll(
            listOf(
                //整个工程默认允许使用 ExperimentalCoroutinesApi(实验api)
                //防止出现实验性api需要添加注释的情况
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            )
        )
        freeCompilerArgs.add(
            /**
             * nia:
             * Remove this args after Phase 3.
             * https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-consistent-copy-visibility/#deprecation-timeline
             *
             * Deprecation timeline
             * Phase 3. (Supposedly Kotlin 2.2 or Kotlin 2.3).
             * The default changes.
             * Unless ExposedCopyVisibility is used, the generated 'copy' method has the same visibility as the primary constructor.
             * The binary signature changes. The error on the declaration is no longer reported.
             * '-Xconsistent-data-class-copy-visibility' compiler flag and ConsistentCopyVisibility annotation are now unnecessary.
             * me:
             * 保证data class的copy函数与主构造函数的可见修饰符一致
             */
            "-Xconsistent-data-class-copy-visibility"
        )



    }
}