import com.android.build.api.dsl.androidLibrary
import com.poulastaa.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KmpLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.multiplatform")
                apply("com.android.kotlin.multiplatform.library")
                apply("com.android.lint")
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
            }

            // Use string-based configuration names to avoid KMP DSL member-extension
            // scoping issues that arise inside sourceSets {} lambdas in compiled plugins.
            dependencies {
                add("commonTestImplementation", libs.findLibrary("kotlin-test").get())
                add("androidDeviceTestImplementation", libs.findLibrary("androidx-core").get())
                add("androidDeviceTestImplementation", libs.findLibrary("androidx-runner").get())
                add("androidDeviceTestImplementation", libs.findLibrary("androidx-testExt-junit").get())
            }
        }
    }
}
