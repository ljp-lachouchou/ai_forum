import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.dsl.Lint
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

class AndroidLintConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            when {
                pluginManager.hasPlugin("com.android.application") ->
                    configure<ApplicationExtension> { lint(Lint::configure) }
                pluginManager.hasPlugin("com.android.library") ->
                    configure<LibraryExtension> { lint(Lint::configure) }
                else -> {
                    // 非安卓模块的备选方案是：应用独立的lint插件。
                    apply(plugin = "com.android.lint")
                    configure<Lint>(Lint::configure)
                }
            }
        }
    }
}
private fun Lint.configure() {
    //启用 XML 格式的 Lint 报告输出
    xmlReport = true
    //启用 SARIF 格式的 Lint 报告.点开某一行，直接跳到代码
    sarifReport = true
    //Lint 不只检查当前 module，还检查它依赖的 modules
    checkDependencies = true
    /*
    禁用 Lint 的 GradleDependency 检查规则
    GradleDependency会检查：
    依赖版本是否过旧
    是否有已知漏洞
    是否有新版本可用
     */
    disable += "GradleDependency"
}