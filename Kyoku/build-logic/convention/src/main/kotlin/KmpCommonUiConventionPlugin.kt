import com.poulastaa.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class KmpCommonUiConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("kyoku.kmp.compose.library")

            dependencies {
                add("commonMainImplementation", libs.findLibrary("material3-window-size-classss").get())
                add("commonMainImplementation", libs.findLibrary("androidx-navigation").get())
                add("commonMainImplementation", libs.findLibrary("koin-core").get())
                add("commonMainImplementation", libs.findLibrary("koin-compose").get())
                add("commonMainImplementation", libs.findLibrary("koin-compose-viewmodel").get())
                add("commonMainImplementation", project(":common:domain"))
            }
        }
    }
}
