package com.poulastaa.common.ui.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

actual val dispatchers: PlatformDispatchers
    get() = object : PlatformDispatchers {
        override val main: CoroutineDispatcher = try {
            Dispatchers.Main
        } catch (_: IllegalArgumentException) {
            Dispatchers.Default
        }
        override val io: CoroutineDispatcher = Dispatchers.IO
    }