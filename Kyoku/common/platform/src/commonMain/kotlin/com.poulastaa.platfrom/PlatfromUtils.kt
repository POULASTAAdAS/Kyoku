package com.poulastaa.platfrom

import com.poulastaa.common.domain.DeviceType
import com.poulastaa.common.domain.PlatformType

expect object PlatformUtils {
    val platform: PlatformType
    val device: DeviceType
    val os: String
    val countryCode: String
}
