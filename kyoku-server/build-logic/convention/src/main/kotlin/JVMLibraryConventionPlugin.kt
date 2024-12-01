import com.poulastaa.convention.configureKotlinJvm
import com.poulastaa.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.repositories

class JVMLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            pluginManager.apply("org.jetbrains.kotlin.jvm")
            configureKotlinJvm()

            repositories {
                mavenCentral()
            }

            dependencies {
                "implementation"(libs.findBundle("kotlin").get())
            }
        }
    }
}