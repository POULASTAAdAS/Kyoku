package com.poulastaa.platfrom

import com.poulastaa.common.domain.PlatformType

actual object PlatformUtils {
    private val osName: String = System.getProperty("os.name").orEmpty()

    actual val platform: PlatformType = when {
        osName.contains("Windows", ignoreCase = true) -> PlatformType.WINDOWS
        osName.contains("Mac", ignoreCase = true) || osName.contains("Darwin", ignoreCase = true) -> PlatformType.MAC
        osName.contains("Linux", ignoreCase = true) -> PlatformType.LINUX
        osName.contains("BSD", ignoreCase = true) || osName.contains("Unix", ignoreCase = true) -> PlatformType.UNIX
        else -> PlatformType.UNKNOWN
    }

    actual val os: String = listOfNotNull(
        osName.takeIf(String::isNotBlank),
        System.getProperty("os.version")?.takeIf(String::isNotBlank),
    ).joinToString(separator = " ").ifBlank { "Unknown" }
}
