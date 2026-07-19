package com.poulastaa.platfrom

import com.poulastaa.common.domain.PlatformType
import platform.Foundation.NSLocale
import platform.Foundation.NSLocaleCountryCode
import platform.Foundation.currentLocale
import platform.UIKit.UIDevice
import platform.UIKit.UIUserInterfaceIdiomPad

actual object PlatformUtils {
    private fun isTab() = UIDevice.currentDevice.userInterfaceIdiom == UIUserInterfaceIdiomPad

    actual val platform: PlatformType = if (isTab()) PlatformType.IOS_TAB else PlatformType.IOS
    actual val os: String = UIDevice.currentDevice.systemVersion
    actual val countryCode: String =
        (NSLocale.currentLocale.objectForKey(NSLocaleCountryCode) as? String).orEmpty()
}
