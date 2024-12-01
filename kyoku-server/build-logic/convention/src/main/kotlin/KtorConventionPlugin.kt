import com.poulastaa.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.repositories

class KtorConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("io.ktor.plugin")
                apply("org.jetbrains.kotlin.plugin.serialization")
            }

            repositories {
                mavenCentral()
            }

            dependencies {
                "implementation"(libs.findBundle("ktor").get())
                "testImplementation"(libs.findBundle("ktor-test").get())
            }
        }
    }
}