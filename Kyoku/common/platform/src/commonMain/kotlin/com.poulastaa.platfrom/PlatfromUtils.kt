package com.poulastaa.platfrom

import com.poulastaa.common.domain.PlatformType

expect object PlatformUtils {
     val platform: PlatformType
     val os: String
     val countryCode: String
}
