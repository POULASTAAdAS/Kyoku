package com.poulastaa.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.BuildType
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

internal fun Project.configureBuildTypes(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
    extensionType: ExtensionType,
) {
    commonExtension.run {
        buildFeatures {
            buildConfig = true
        }

        val baseUrl = gradleLocalProperties(rootDir, providers).getProperty("BASE_URL")
        val clientId = gradleLocalProperties(rootDir, providers).getProperty("CLIENT_ID")

        when (extensionType) {
            ExtensionType.APPLICATION -> extensions.configure<ApplicationExtension> {
                buildTypes {
                    debug {
                        configureDebugBuildType(baseUrl, clientId)
                    }

                    release {
                        configureReleaseBuildType(
                            commonExtension,
                            baseUrl,
                            clientId
                        )
                    }
                }
            }

            ExtensionType.LIBRARY -> extensions.configure<LibraryExtension> {
                buildTypes {
                    debug {
                        configureDebugBuildType(baseUrl, clientId)
                    }

                    release {
                        configureReleaseBuildType(
                            commonExtension,
                            baseUrl,
                            clientId
                        )
                    }
                }
            }
        }
    }
}


private fun BuildType.configureDebugBuildType(
    baseUrl: String,
    clientId: String,
) {
    buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
    buildConfigField("String", "CLIENT_ID", "\"$clientId\"")
}

private fun BuildType.configureReleaseBuildType(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
    baseUrl: String,
    clientId: String,
) {
    buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
    buildConfigField("String", "CLIENT_ID", "\"$clientId\"")

    isMinifyEnabled = true
    proguardFiles(
        commonExtension.getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
    )
}