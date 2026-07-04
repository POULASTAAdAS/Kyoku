import com.poulastaa.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class KmpNetworkConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("kyoku.kmp.library")
            pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")

            dependencies {
                add("commonMainImplementation", libs.findLibrary("ktor-client-core").get())
                add(
                    "commonMainImplementation",
                    libs.findLibrary("ktor-client-content-negotiation").get()
                )
                add(
                    "commonMainImplementation",
                    libs.findLibrary("ktor-client-serialization-kotlinx-json").get()
                )
                add("androidMainImplementation", libs.findLibrary("ktor-client-okhttp").get())
                add("iosX64MainImplementation", libs.findLibrary("ktor-client-darwin").get())
                add("iosArm64MainImplementation", libs.findLibrary("ktor-client-darwin").get())
                add(
                    "iosSimulatorArm64MainImplementation",
                    libs.findLibrary("ktor-client-darwin").get()
                )
                add("jvmMainImplementation", libs.findLibrary("ktor-client-cio").get())
            }
        }
    }
}
