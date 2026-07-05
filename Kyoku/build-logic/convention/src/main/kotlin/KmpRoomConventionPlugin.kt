import androidx.room.gradle.RoomExtension
import com.android.build.gradle.LibraryExtension
import com.poulastaa.convention.libs
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KmpRoomConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.multiplatform")
                apply("com.android.library")
                apply("androidx.room")
                apply("com.google.devtools.ksp")
                apply("kyoku.kmp.koin")
            }

            extensions.configure<KotlinMultiplatformExtension> {
                androidTarget {
                    compilerOptions {
                        jvmTarget.set(JvmTarget.JVM_11)
                    }
                }

                iosX64()
                iosArm64()
                iosSimulatorArm64()
                jvm()
            }

            extensions.configure<LibraryExtension> {
                compileSdk = libs.findVersion("android-compileSdk").get().toString().toInt()

                defaultConfig {
                    minSdk = libs.findVersion("android-minSdk").get().toString().toInt()
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }

                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_11
                    targetCompatibility = JavaVersion.VERSION_11
                }
            }

            extensions.configure<RoomExtension> {
                schemaDirectory("$projectDir/schemas")
            }

            dependencies {
                add("commonMainImplementation", libs.findLibrary("androidx-room-runtime").get())
                add("commonMainImplementation", libs.findLibrary("androidx-sqlite-bundled").get())

                val roomCompiler = libs.findLibrary("androidx-room-compiler").get()
                add("kspAndroid", roomCompiler)
                add("kspIosX64", roomCompiler)
                add("kspIosArm64", roomCompiler)
                add("kspIosSimulatorArm64", roomCompiler)
                add("kspJvm", roomCompiler)

                add("commonTestImplementation", libs.findLibrary("kotlin-test").get())
                add("androidInstrumentedTestImplementation", libs.findLibrary("androidx-core").get())
                add("androidInstrumentedTestImplementation", libs.findLibrary("androidx-runner").get())
                add(
                    "androidInstrumentedTestImplementation",
                    libs.findLibrary("androidx-testExt-junit").get()
                )
            }
        }
    }
}
