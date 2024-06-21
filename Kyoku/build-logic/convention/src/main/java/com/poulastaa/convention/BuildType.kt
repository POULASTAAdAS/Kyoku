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

        val authUrl = gradleLocalProperties(rootDir, providers).getProperty("AUTH_BASE_URL")
        val serviceUrl = gradleLocalProperties(rootDir, providers).getProperty("SERVICE_BASE_URL")
        val clientId = gradleLocalProperties(rootDir, providers).getProperty("CLIENT_ID")

        when (extensionType) {
            ExtensionType.APPLICATION -> {
                extensions.configure<ApplicationExtension> {
                    buildTypes {
                        debug {
                            configureDebugBuildType(authUrl, serviceUrl,clientId)
                        }
                        release {
                            configureReleaseBuildType(commonExtension, authUrl, serviceUrl,clientId)
                        }
                    }
                }
            }

            ExtensionType.LIBRARY -> {
                extensions.configure<LibraryExtension> {
                    buildTypes {
                        debug {
                            configureDebugBuildType(authUrl, serviceUrl,clientId)
                        }
                        release {
                            configureReleaseBuildType(commonExtension, authUrl, serviceUrl,clientId)
                        }
                    }
                }
            }
        }
    }
}

private fun BuildType.configureDebugBuildType(
    authUrl: String,
    serviceUrl: String,
    clientId: String,
) {
    buildConfigField("String", "AUTH_BASE_URL", "\"$authUrl\"")
    buildConfigField("String", "SERVICE_BASE_URL", "\"$serviceUrl\"")
    buildConfigField("String", "CLIENT_ID", "\"$clientId\"")
}

private fun BuildType.configureReleaseBuildType(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
    authUrl: String,
    serviceUrl: String,
    clientId: String,
) {
    buildConfigField("String", "AUTH_BASE_URL", "\"$authUrl\"")
    buildConfigField("String", "SERVICE_BASE_URL", "\"$serviceUrl\"")
    buildConfigField("String", "CLIENT_ID", "\"$clientId\"")

    isMinifyEnabled = true
    proguardFiles(
        commonExtension.getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
    )
}