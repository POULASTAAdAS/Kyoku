package com.poulastaa.common.network.utils

import com.poulastaa.common.domain.PlatformType
import com.poulastaa.platfrom.PlatformUtils

actual object NetworkPlatformUtils {
    actual fun getPlatformUserAgent(): String {
        val platform = when (PlatformUtils.platform) {
            PlatformType.WINDOWS -> "Windows NT 10.0; Win64; x64"
            PlatformType.MAC -> "Macintosh; Intel Mac OS X ${sanitizeUserAgentHeaderValue(PlatformUtils.os).replace('.', '_')}"
            PlatformType.LINUX -> "X11; Linux x86_64"
            PlatformType.UNIX -> "X11; Unix x86_64"
            else -> "X11; Unknown x86_64"
        }

        return "Mozilla/5.0 ($platform) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.6998.135 Safari/537.36"
    }
}
