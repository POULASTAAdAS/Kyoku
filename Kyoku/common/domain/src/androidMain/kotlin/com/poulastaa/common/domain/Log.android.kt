package com.poulastaa.common.domain

import android.util.Log as AndroidLog

internal actual val platformLogger: PlatformLogger = AndroidPlatformLogger

private object AndroidPlatformLogger : PlatformLogger {
    override fun d(tag: String, msg: String): Int = AndroidLog.d(tag, msg)

    override fun d(tag: String, msg: String, tr: Throwable): Int = AndroidLog.d(tag, msg, tr)

    override fun e(tag: String, msg: String): Int = AndroidLog.e(tag, msg)

    override fun e(tag: String, msg: String, tr: Throwable): Int = AndroidLog.e(tag, msg, tr)

    override fun i(tag: String, msg: String): Int = AndroidLog.i(tag, msg)

    override fun i(tag: String, msg: String, tr: Throwable): Int = AndroidLog.i(tag, msg, tr)
}
