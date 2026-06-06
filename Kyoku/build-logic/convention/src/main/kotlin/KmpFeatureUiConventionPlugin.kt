import com.poulastaa.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class KmpFeatureUiConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("kyoku.kmp.compose.library")

            dependencies {
                add("commonMainImplementation", libs.findLibrary("androidx-navigation").get())
                add("commonMainImplementation", libs.findLibrary("koin-core").get())
                add("commonMainImplementation", libs.findLibrary("koin-compose").get())
                add("commonMainImplementation", libs.findLibrary("koin-compose-viewmodel").get())
                add("commonMainImplementation", project(":common:ui"))
                add("commonMainImplementation", project(":common:domain"))
            }
        }
    }
}
