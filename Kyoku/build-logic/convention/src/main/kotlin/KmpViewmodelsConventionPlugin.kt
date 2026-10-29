import com.android.build.api.dsl.LibraryExtension
import com.poulastaa.convention.addCommonNetworkDependency
import com.poulastaa.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KmpViewmodelsConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                // Use com.android.library (not com.android.kotlin.multiplatform.library) so that
                // Compose Multiplatform resources are properly packaged into Android assets.
                // Compose is applied only to generate string resources; no Compose UI dependencies.
                // The Compose compiler plugin is required by the Compose plugin and needs compose-runtime.
                apply("org.jetbrains.kotlin.multiplatform")
                apply("com.android.library")
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("org.jetbrains.kotlin.plugin.serialization")
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
                add("commonMainApi", libs.findLibrary("androidx-viewmodel").get())
                add("commonMainImplementation", libs.findLibrary("compose-runtime").get())
                add(
                    "commonMainImplementation",
                    libs.findLibrary("compose-components-resources").get()
                )
                add("commonMainApi", libs.findLibrary("kotlinx-serialization-core").get())
                add("commonMainImplementation", libs.findLibrary("koin-core").get())
                add("commonMainImplementation", project(":common:domain"))
                addCommonNetworkDependency()

                add("commonTestImplementation", libs.findLibrary("kotlin-test").get())
            }
        }
    }
}
