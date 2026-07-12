package com.poulastaa.common.domain

object Log {
    fun d(tag: String, msg: String): Int = log { platformLogger.d(tag, msg) }

    fun d(tag: String, msg: String, tr: Throwable): Int = log { platformLogger.d(tag, msg, tr) }

    fun e(tag: String, msg: String): Int = log { platformLogger.e(tag, msg) }

    fun e(tag: String, msg: String, tr: Throwable): Int = log { platformLogger.e(tag, msg, tr) }

    fun i(tag: String, msg: String): Int = log { platformLogger.i(tag, msg) }

    fun i(tag: String, msg: String, tr: Throwable): Int = log { platformLogger.i(tag, msg, tr) }

    private inline fun log(block: () -> Int): Int = if (SharedConfig.IS_DEBUG) block() else 0
}

internal interface PlatformLogger {
    fun d(tag: String, msg: String): Int
    fun d(tag: String, msg: String, tr: Throwable): Int

    fun e(tag: String, msg: String): Int
    fun e(tag: String, msg: String, tr: Throwable): Int

    fun i(tag: String, msg: String): Int
    fun i(tag: String, msg: String, tr: Throwable): Int
}

internal expect val platformLogger: PlatformLogger
