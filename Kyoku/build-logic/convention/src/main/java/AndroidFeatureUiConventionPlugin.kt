import com.poulastaa.convention.addUiLayerDependencies
import com.poulastaa.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureUiConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("kyoku.android.library.compose")
                apply(libs.findPlugin("kotlinx-serialization").get().get().pluginId)
            }

            dependencies {
                addUiLayerDependencies(target)
            }
        }
    }
}