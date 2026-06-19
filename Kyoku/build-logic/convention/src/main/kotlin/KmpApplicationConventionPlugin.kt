import com.android.build.api.dsl.ApplicationExtension
import com.poulastaa.convention.libs
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KmpApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.multiplatform")
                apply("com.android.application")
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("org.jetbrains.compose.hot-reload")
            }

            extensions.configure<KotlinMultiplatformExtension> {
                androidTarget {
                    compilerOptions {
                        jvmTarget.set(JvmTarget.JVM_11)
                    }
                }

                listOf(
                    iosArm64(),
                    iosSimulatorArm64()
                ).forEach { iosTarget ->
                    iosTarget.binaries.framework {
                        baseName = "ComposeApp"
                        isStatic = true
                    }
                }
                jvm()
            }

            extensions.configure<ApplicationExtension> {
                compileSdk = libs.findVersion("android-compileSdk").get().toString().toInt()

                defaultConfig {
                    minSdk = libs.findVersion("android-minSdk").get().toString().toInt()
                }

                packaging {
                    resources {
                        excludes += "/META-INF/{AL2.0,LGPL2.1}"
                    }
                }

                buildTypes {
                    getByName("release") {
                        isMinifyEnabled = false
                    }
                }

                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_11
                    targetCompatibility = JavaVersion.VERSION_11
                }
            }

            dependencies {
                add("commonMainImplementation", libs.findLibrary("compose-runtime").get())
                add("commonMainImplementation", libs.findLibrary("compose-foundation").get())
                add("commonMainImplementation", libs.findLibrary("compose-material3").get())
                add("commonMainImplementation", libs.findLibrary("compose-ui").get())
                add("commonMainImplementation", libs.findLibrary("compose-ui-backhandler").get())
                add(
                    "commonMainImplementation",
                    libs.findLibrary("compose-components-resources").get()
                )
                add("commonMainImplementation", libs.findLibrary("compose-uiToolingPreview").get())
                add(
                    "commonMainImplementation",
                    libs.findLibrary("androidx-lifecycle-viewmodelCompose").get()
                )
                add(
                    "commonMainImplementation",
                    libs.findLibrary("androidx-lifecycle-runtimeCompose").get()
                )
                add(
                    "commonMainImplementation",
                    libs.findLibrary("material3-window-size-classss").get()
                )
                add("commonMainImplementation", libs.findLibrary("androidx-viewmodel").get())
                add("commonMainImplementation", libs.findLibrary("androidx-navigation").get())
                add("commonMainImplementation", libs.findLibrary("koin-core").get())
                add("commonMainImplementation", libs.findLibrary("koin-compose").get())
                add("commonMainImplementation", libs.findLibrary("koin-compose-viewmodel").get())

                add("androidMainImplementation", libs.findLibrary("compose-uiToolingPreview").get())
                add(
                    "androidMainImplementation",
                    libs.findLibrary("androidx-activity-compose").get()
                )
                add("androidMainImplementation", libs.findLibrary("koin-android").get())

                add("jvmMainImplementation", libs.findLibrary("kotlinx-coroutinesSwing").get())

                add("debugImplementation", libs.findLibrary("androidx-compose-uiTooling").get())
            }
        }
    }
}
