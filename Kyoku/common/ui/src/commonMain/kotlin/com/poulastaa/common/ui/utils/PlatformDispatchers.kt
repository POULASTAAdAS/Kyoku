package com.poulastaa.common.ui.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface PlatformDispatchers {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
}

expect val dispatchers: PlatformDispatchers

val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
val ioDispatcher: CoroutineDispatcher = dispatchers.io
val mainDispatcher: CoroutineDispatcher = dispatchers.main