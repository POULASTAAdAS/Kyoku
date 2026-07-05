package com.poulastaa.common.network.utils

expect object NetworkPlatformUtils {
    fun getPlatformUserAgent(): String
}

internal fun sanitizeUserAgentHeaderValue(value: String): String = buildString(value.length) {
    value.forEach { char ->
        append(
            when (char) {
                '\r', '\n', '\u0000' -> ' '
                else -> char
            }
        )
    }
}.trim()
