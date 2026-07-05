package com.poulastaa.common.network.utils

import com.poulastaa.common.domain.PlatformType
import com.poulastaa.platfrom.PlatformUtils

actual object NetworkPlatformUtils {
    actual fun getPlatformUserAgent(): String {
        val osVersion = sanitizeUserAgentHeaderValue(PlatformUtils.os)

        return when (PlatformUtils.platform) {
            PlatformType.ANDROID_TAB -> "Mozilla/5.0 (Linux; Android $osVersion; Tablet) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.6998.135 Safari/537.36"
            else -> "Mozilla/5.0 (Linux; Android $osVersion) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.6998.135 Mobile Safari/537.36"
        }
    }
}
