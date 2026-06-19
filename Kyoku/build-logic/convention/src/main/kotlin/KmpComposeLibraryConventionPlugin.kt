import com.poulastaa.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class KmpComposeLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("kyoku.kmp.library")

            with(pluginManager) {
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("org.jetbrains.compose.hot-reload")
            }

            dependencies {
                add("commonMainImplementation", libs.findLibrary("compose-material3").get())
                add("commonMainImplementation", libs.findLibrary("compose-ui").get())
                add("commonMainImplementation", libs.findLibrary("compose-ui-backhandler").get())
                add("commonMainImplementation", libs.findLibrary("compose-uiToolingPreview").get())
                add(
                    "commonMainImplementation",
                    libs.findLibrary("compose-components-resources").get()
                )
                add("androidRuntimeClasspath", libs.findLibrary("androidx-compose-uiTooling").get())
            }
        }
    }
}
