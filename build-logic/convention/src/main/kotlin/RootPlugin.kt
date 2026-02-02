import ai.ljp.convention.configureGraphTasks
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * 生成依赖图
 */
class RootPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        //根项目
        require(target.path==":")

        target.subprojects { configureGraphTasks() }
    }
}