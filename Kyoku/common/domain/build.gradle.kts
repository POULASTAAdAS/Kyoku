import java.util.Properties

plugins {
    alias(libs.plugins.kyoku.kmp.library)
    alias(libs.plugins.buildConfig)
}

val sharedPropertiesFile = rootProject.file("shared.properties")
val sharedProperties = Properties().apply {
    sharedPropertiesFile.inputStream().use(::load)
}

fun requiredSharedProperty(key: String): String =
    sharedProperties.getProperty(key) ?: error("Missing `$key` in $sharedPropertiesFile")

// Release tasks must not print app logs even if shared.properties keeps debug=true locally.
val isReleaseBuild = gradle.startParameter.taskNames.any { taskName ->
    taskName.contains("Release", ignoreCase = true)
}

kotlin {
    androidLibrary {
        namespace = "com.poulastaa.common.domain"
    }
}

buildConfig {
    packageName("com.poulastaa.common.domain")
    className("SharedConfig")
    useKotlinOutput { internalVisibility = false }

    buildConfigField("BASE_URL", requiredSharedProperty("baseUrl"))
    buildConfigField("GOOGLE_WEB_CLIENT_ID", requiredSharedProperty("googleWebClientId"))
    buildConfigField(
        "IS_DEBUG",
        !isReleaseBuild && requiredSharedProperty("debug").toBooleanStrict()
    )
}
