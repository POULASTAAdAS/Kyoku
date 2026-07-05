import com.poulastaa.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class KmpKoinConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("io.insert-koin.compiler.plugin")

            dependencies {
                add("commonMainImplementation", libs.findLibrary("koin-core").get())
                add("commonMainImplementation", libs.findLibrary("koin-annotations").get())
            }
        }
    }
}
