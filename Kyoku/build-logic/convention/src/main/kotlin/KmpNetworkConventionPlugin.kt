import com.android.build.api.dsl.androidLibrary
import com.poulastaa.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KmpNetworkConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.multiplatform")
                apply("com.android.kotlin.multiplatform.library")
                apply("com.android.lint")
                apply("org.jetbrains.kotlin.plugin.serialization")
                apply("kyoku.kmp.koin")
            }

            extensions.configure<KotlinMultiplatformExtension> {
                androidLibrary {
                    compileSdk = libs.findVersion("android-compileSdk").get().toString().toInt()
                    minSdk = libs.findVersion("android-minSdk").get().toString().toInt()

                    withHostTestBuilder {}

                    withDeviceTestBuilder {
                        sourceSetTreeName = "test"
                    }.configure {
                        instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                    }
                }

                iosX64()
                iosArm64()
                iosSimulatorArm64()
                jvm()

                sourceSets.configureEach {
                    if (name == "iosMain") {
                        dependencies {
                            implementation(libs.findLibrary("ktor-client-darwin").get())
                        }
                    }
                }
            }

            dependencies {
                add("commonMainImplementation", libs.findLibrary("ktor-client-core").get())
                add(
                    "commonMainImplementation",
                    libs.findLibrary("ktor-client-content-negotiation").get()
                )
                add(
                    "commonMainImplementation",
                    libs.findLibrary("ktor-client-serialization-kotlinx-json").get()
                )
                add("androidMainImplementation", libs.findLibrary("ktor-client-okhttp").get())
                add("jvmMainImplementation", libs.findLibrary("ktor-client-cio").get())
                add("commonTestImplementation", libs.findLibrary("kotlin-test").get())
                add("androidDeviceTestImplementation", libs.findLibrary("androidx-core").get())
                add("androidDeviceTestImplementation", libs.findLibrary("androidx-runner").get())
                add(
                    "androidDeviceTestImplementation",
                    libs.findLibrary("androidx-testExt-junit").get()
                )
            }
        }
    }
}
