package com.poulastaa.common.ui.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual val dispatchers: PlatformDispatchers
    get() = object : PlatformDispatchers {
        override val main: CoroutineDispatcher = Dispatchers.Main
        override val io: CoroutineDispatcher = Dispatchers.IO
    }