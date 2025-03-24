import com.poulastaa.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidDaggerHiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("com.google.devtools.ksp")
                apply("dagger.hilt.android.plugin")
            }

            dependencies {
                "implementation"(libs.findLibrary("dagger.hilt").get())
                "implementation"(libs.findLibrary("hilt.navigation.compose").get())
                "ksp"(libs.findLibrary("dagger.hilt.compiler").get())
                "ksp"(libs.findLibrary("hilt.compiler").get())
            }
        }
    }
}