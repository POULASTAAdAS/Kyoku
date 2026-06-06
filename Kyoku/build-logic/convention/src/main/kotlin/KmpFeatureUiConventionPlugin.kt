import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class KmpFeatureUiConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("kyoku.kmp.common.ui")

            dependencies {
                add("commonMainImplementation", project(":common:ui"))
            }
        }
    }
}
