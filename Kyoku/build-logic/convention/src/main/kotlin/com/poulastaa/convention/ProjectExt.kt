package com.poulastaa.convention

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

fun Project.addCommonNetworkDependency() {
    if (path != ":common:network") {
        dependencies.add("commonMainImplementation", project(":common:network"))
    }
}
