package com.poulastaa.common.ui.image

import coil3.PlatformContext
import kotlinx.cinterop.ExperimentalForeignApi
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
internal actual fun PlatformContext.imageCacheDir(): Path = NSFileManager.defaultManager
    .URLForDirectory(
        directory = NSCachesDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = true,
        error = null,
    )?.path?.toPath() ?: FileSystem.SYSTEM_TEMPORARY_DIRECTORY
