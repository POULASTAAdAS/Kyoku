package com.poulastaa.common.network.utils

import com.poulastaa.common.domain.PlatformType
import com.poulastaa.platfrom.PlatformUtils

actual object NetworkPlatformUtils {
    actual fun getPlatformUserAgent(): String {
        val os = sanitizeUserAgentHeaderValue(PlatformUtils.os)
        val osVersion = os.replace('.', '_')

        return when (PlatformUtils.platform) {
            PlatformType.IOS_TAB -> "Mozilla/5.0 (iPad; CPU OS $osVersion like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/$os Mobile/15E148 Safari/604.1"
            else -> "Mozilla/5.0 (iPhone; CPU iPhone OS $osVersion like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/$os Mobile/15E148 Safari/604.1"
        }
    }
}
