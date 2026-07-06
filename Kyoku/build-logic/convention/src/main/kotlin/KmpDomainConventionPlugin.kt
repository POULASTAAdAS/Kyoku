import com.poulastaa.convention.addCommonNetworkDependency
import org.gradle.api.Plugin
import org.gradle.api.Project

class KmpDomainConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("kyoku.kmp.library")
            addCommonNetworkDependency()
        }
    }
}
