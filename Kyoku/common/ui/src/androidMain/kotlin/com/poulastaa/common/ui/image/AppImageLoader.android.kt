package com.poulastaa.common.ui.image

import coil3.PlatformContext
import okio.Path
import okio.Path.Companion.toOkioPath

internal actual fun PlatformContext.imageCacheDir(): Path = cacheDir.toOkioPath()
