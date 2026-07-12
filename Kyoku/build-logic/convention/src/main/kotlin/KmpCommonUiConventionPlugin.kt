import com.android.build.api.dsl.LibraryExtension
import com.poulastaa.convention.addCommonNetworkDependency
import com.poulastaa.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KmpCommonUiConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                // Use com.android.library (not com.android.kotlin.multiplatform.library) so that
                // Compose Multiplatform resources are properly packaged into Android assets.
                apply("org.jetbrains.kotlin.multiplatform")
                apply("com.android.library")
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("org.jetbrains.compose.hot-reload")
            }

            extensions.configure<KotlinMultiplatformExtension> {
                compilerOptions {
                    freeCompilerArgs.add("-Xexpect-actual-classes")
                }

                androidTarget()
                iosX64()
                iosArm64()
                iosSimulatorArm64()
                jvm()
            }

            extensions.configure<LibraryExtension> {
                compileSdk = libs.findVersion("android-compileSdk").get().toString().toInt()
                defaultConfig {
                    minSdk = libs.findVersion("android-minSdk").get().toString().toInt()
                }
            }

            dependencies {
                add("commonMainImplementation", libs.findLibrary("compose-material3").get())
                add("commonMainImplementation", libs.findLibrary("compose-ui").get())
                add("commonMainImplementation", libs.findLibrary("compose-ui-backhandler").get())
                add("commonMainImplementation", libs.findLibrary("compose-uiToolingPreview").get())
                add(
                    "commonMainImplementation",
                    libs.findLibrary("compose-components-resources").get()
                )
                add(
                    "commonMainImplementation",
                    libs.findLibrary("material3-window-size-classss").get()
                )
                add("commonMainImplementation", libs.findLibrary("androidx-navigation").get())
                add("commonMainImplementation", libs.findLibrary("koin-core").get())
                add("commonMainImplementation", libs.findLibrary("koin-compose").get())
                add("commonMainImplementation", libs.findLibrary("koin-compose-viewmodel").get())
                add("commonMainImplementation", project(":common:domain"))
                addCommonNetworkDependency()

                add("commonTestImplementation", libs.findLibrary("kotlin-test").get())
                add("debugImplementation", libs.findLibrary("androidx-compose-uiTooling").get())
            }
        }
    }
}
