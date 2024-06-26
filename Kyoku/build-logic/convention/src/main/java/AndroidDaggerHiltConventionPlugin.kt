import com.poulastaa.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidDaggerHiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("dagger.hilt.android.plugin")
                apply("kotlin-kapt")
            }

            dependencies {
                "implementation"(libs.findLibrary("dagger.hilt").get())
                "implementation"(libs.findLibrary("hilt.navigation.compose").get())
                "kapt"(libs.findLibrary("dagger.hilt.compiler").get())
                "kapt"(libs.findLibrary("hilt.compiler").get())
            }
        }
    }
}