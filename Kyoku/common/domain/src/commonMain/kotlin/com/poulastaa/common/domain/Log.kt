package com.poulastaa.common.domain

object Log {
    fun d(tag: String, msg: String): Int = platformLogger.d(tag, msg)

    fun d(tag: String, msg: String, tr: Throwable): Int = platformLogger.d(tag, msg, tr)

    fun e(tag: String, msg: String): Int = platformLogger.e(tag, msg)

    fun e(tag: String, msg: String, tr: Throwable): Int = platformLogger.e(tag, msg, tr)

    fun i(tag: String, msg: String): Int = platformLogger.i(tag, msg)

    fun i(tag: String, msg: String, tr: Throwable): Int = platformLogger.i(tag, msg, tr)
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
