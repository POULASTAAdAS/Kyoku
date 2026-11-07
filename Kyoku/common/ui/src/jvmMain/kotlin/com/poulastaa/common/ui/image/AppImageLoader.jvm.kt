package com.poulastaa.common.ui.image

import coil3.PlatformContext
import okio.FileSystem
import okio.Path

internal actual fun PlatformContext.imageCacheDir(): Path =
    FileSystem.SYSTEM_TEMPORARY_DIRECTORY.resolve("kyoku")
