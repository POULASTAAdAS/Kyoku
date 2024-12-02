import com.poulastaa.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaApplication
import org.gradle.kotlin.dsl.dependencies

class KtorConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("kyoku.jvm.library")

                apply("io.ktor.plugin")
                apply("org.jetbrains.kotlin.plugin.serialization")
            }

            dependencies {
                "implementation"(libs.findBundle("ktor").get())
                "testImplementation"(libs.findBundle("ktor-test").get())
            }

            extensions.configure(JavaApplication::class.java) {
                mainClass.set("io.ktor.server.netty.EngineMain")

                val isDevelopment: Boolean = project.hasProperty("development")
                applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
            }
        }
    }
}