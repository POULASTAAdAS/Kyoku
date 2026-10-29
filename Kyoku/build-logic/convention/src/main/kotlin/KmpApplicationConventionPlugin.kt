import com.android.build.api.dsl.ApplicationExtension
import com.poulastaa.convention.addCommonNetworkDependency
import com.poulastaa.convention.libs
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import java.util.Properties

class KmpApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val iosUi = resolveIosUi()
            writeIosUiXcconfig(iosUi)

            with(pluginManager) {
                apply("org.jetbrains.kotlin.multiplatform")
                apply("com.android.application")
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("org.jetbrains.compose.hot-reload")
                apply("io.insert-koin.compiler.plugin")
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
                        export(project(":auth:ui"))
                        export(project(":common:viewmodels"))
                        export(project(":auth:viewmodels"))
                    }
                }
                jvm()

                // The Compose iOS entry point is only compiled when the iOS app uses Compose UI.
                if (iosUi == IOS_UI_COMPOSE) {
                    sourceSets.configureEach {
                        if (name == "iosMain") {
                            kotlin.srcDir("src/iosComposeMain/kotlin")
                        }
                    }
                }
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
                add("commonMainImplementation", libs.findLibrary("koin-core-coroutines").get())
                add("commonMainImplementation", libs.findLibrary("koin-annotations").get())
                add("commonMainImplementation", libs.findLibrary("koin-compose").get())
                add("commonMainImplementation", libs.findLibrary("koin-compose-viewmodel").get())
                addCommonNetworkDependency()

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

private const val IOS_UI_PROPERTY = "iosUi"
private const val IOS_UI_SWIFTUI = "swiftui"
private const val IOS_UI_COMPOSE = "compose"

/**
 * Resolves the iOS UI flag: Gradle property (`-PiosUi=…`), else `shared.properties`, else SwiftUI.
 */
private fun Project.resolveIosUi(): String {
    val sharedPropertiesFile = rootProject.file("shared.properties")
    val sharedValue = if (sharedPropertiesFile.exists()) {
        Properties().apply {
            sharedPropertiesFile.inputStream().use(::load)
        }.getProperty(IOS_UI_PROPERTY)
    } else null

    val iosUi = (providers.gradleProperty(IOS_UI_PROPERTY).orNull ?: sharedValue ?: IOS_UI_SWIFTUI)
        .trim()
        .lowercase()

    require(iosUi == IOS_UI_SWIFTUI || iosUi == IOS_UI_COMPOSE) {
        "Unknown `$IOS_UI_PROPERTY` value '$iosUi'. Allowed values: $IOS_UI_SWIFTUI | $IOS_UI_COMPOSE"
    }

    return iosUi
}

/**
 * Keeps the Xcode side in sync with the flag. Included from `iosApp/Configuration/Config.xcconfig`.
 */
private fun Project.writeIosUiXcconfig(iosUi: String) {
    val xcconfig = rootProject.file("iosApp/Configuration/IosUi.generated.xcconfig")
    val conditions = if (iosUi == IOS_UI_SWIFTUI) "\$(inherited) KYOKU_SWIFTUI" else "\$(inherited)"
    val content = buildString {
        appendLine("// Generated by KmpApplicationConventionPlugin from the `$IOS_UI_PROPERTY` Gradle flag. Do not edit.")
        appendLine("KYOKU_IOS_UI = $iosUi")
        appendLine("SWIFT_ACTIVE_COMPILATION_CONDITIONS = $conditions")
    }

    if (xcconfig.exists() && xcconfig.readText() == content) return
    xcconfig.parentFile.mkdirs()
    xcconfig.writeText(content)
}
