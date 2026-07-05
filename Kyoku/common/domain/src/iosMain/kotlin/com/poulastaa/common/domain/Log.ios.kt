package com.poulastaa.common.domain

internal actual val platformLogger: PlatformLogger = IosPlatformLogger

private object IosPlatformLogger : PlatformLogger {
    override fun d(tag: String, msg: String): Int = printlnLog("DEBUG", tag, msg)

    override fun d(tag: String, msg: String, tr: Throwable): Int = printlnLog("DEBUG", tag, msg, tr)

    override fun e(tag: String, msg: String): Int = printlnLog("ERROR", tag, msg)

    override fun e(tag: String, msg: String, tr: Throwable): Int = printlnLog("ERROR", tag, msg, tr)

    override fun i(tag: String, msg: String): Int = printlnLog("INFO", tag, msg)

    override fun i(tag: String, msg: String, tr: Throwable): Int = printlnLog("INFO", tag, msg, tr)
}

private fun printlnLog(level: String, tag: String, msg: String, tr: Throwable? = null): Int {
    val message = "$level/$tag: $msg"
    println(message)
    tr?.printStackTrace()

    return message.length
}
