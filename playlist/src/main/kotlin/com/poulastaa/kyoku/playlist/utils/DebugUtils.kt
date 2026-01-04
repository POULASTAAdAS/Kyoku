package com.poulastaa.kyoku.playlist.utils

import org.slf4j.LoggerFactory

object DebugUtils {
    const val IS_DEBUG = true

    val LOGGER = LoggerFactory.getLogger(this::class.java)

    fun d(message: String) = if (IS_DEBUG) LOGGER.debug(message) else Unit
    fun e(message: String) = if (IS_DEBUG) LOGGER.error(message) else Unit
}