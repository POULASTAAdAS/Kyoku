import com.poulastaa.convention.configureKotlinJvm
import org.gradle.api.Plugin
import org.gradle.api.Project

class JVMLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            pluginManager.apply("org.jetbrains.kotlin.jvm")

            configureKotlinJvm()
        }
    }
}