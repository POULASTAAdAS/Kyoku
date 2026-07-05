package com.poulastaa.platfrom

import android.content.res.Resources
import android.os.Build
import com.poulastaa.common.domain.PlatformType

actual object PlatformUtils {
    private fun isAndroidTab() = Resources.getSystem().configuration.smallestScreenWidthDp >= 600

    actual val platform: PlatformType = if (isAndroidTab()) PlatformType.ANDROID_TAB else PlatformType.ANDROID
    actual val os: String = Build.VERSION.RELEASE
}
