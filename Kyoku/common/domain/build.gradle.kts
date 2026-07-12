import java.util.Properties

abstract class GenerateSharedConfig : DefaultTask() {
    // Keep inputs/outputs explicit so Gradle can cache and skip this generator safely.
    @get:InputFile
    abstract val sharedPropertiesFile: RegularFileProperty

    @get:Input
    abstract val releaseBuild: Property<Boolean>

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @TaskAction
    fun generate() {
        // Convert shared.properties into common Kotlin constants available to every KMP target.
        val sharedProperties = Properties().apply {
            sharedPropertiesFile.get().asFile.inputStream().use(::load)
        }

        fun requiredSharedProperty(key: String): String =
            sharedProperties.getProperty(key)
                ?: error("Missing `$key` in ${sharedPropertiesFile.get().asFile}")

        val apiBaseUrl = requiredSharedProperty("baseUrl")
        val isDebug = !releaseBuild.get() && requiredSharedProperty("debug").toBooleanStrict()
        val outputFile = outputDir.get()
            .file("com/poulastaa/common/domain/SharedConfig.kt")
            .asFile

        outputFile.parentFile.mkdirs()
        outputFile.writeText(
            """
            |package com.poulastaa.common.domain
            |
            |object SharedConfig {
            |    const val BASE_URL: String = ${apiBaseUrl.toKotlinStringLiteral()}
            |    const val IS_DEBUG: Boolean = $isDebug
            |}
            |
            """.trimMargin()
        )
    }

    // Values from shared.properties are written inside generated Kotlin source.
    private fun String.toKotlinStringLiteral(): String = buildString {
        append('"')
        this@toKotlinStringLiteral.forEach { char ->
            when (char) {
                '\\' -> append("\\\\")
                '"' -> append("\\\"")
                '\n' -> append("\\n")
                '\r' -> append("\\r")
                '\t' -> append("\\t")
                else -> append(char)
            }
        }
        append('"')
    }
}

plugins {
    alias(libs.plugins.kyoku.kmp.library)
}

val rootSharedPropertiesFile = rootProject.layout.projectDirectory.file("shared.properties")
// Release tasks must not print app logs even if shared.properties keeps debug=true locally.
val isReleaseBuild = gradle.startParameter.taskNames.any { taskName ->
    taskName.contains("Release", ignoreCase = true)
}

val generatedSharedConfigDir = layout.buildDirectory.dir("generated/source/sharedConfig/commonMain/kotlin")
val generateSharedConfig by tasks.registering(GenerateSharedConfig::class) {
    sharedPropertiesFile.set(rootSharedPropertiesFile)
    releaseBuild.set(isReleaseBuild)
    outputDir.set(generatedSharedConfigDir)
}

kotlin {
    androidLibrary {
        namespace = "com.poulastaa.common.domain"
    }

    sourceSets {
        commonMain {
            // Generated SharedConfig is common code, so network/domain can use it on all targets.
            kotlin.srcDir(generatedSharedConfigDir)
        }
    }
}

// Android KMP compile tasks are named compileAndroidMain, so depend on all compile tasks.
tasks.matching { task ->
    task.name.startsWith("compile")
}.configureEach {
    dependsOn(generateSharedConfig)
}
