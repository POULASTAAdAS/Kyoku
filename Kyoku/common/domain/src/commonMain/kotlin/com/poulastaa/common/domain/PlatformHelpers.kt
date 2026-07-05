package com.poulastaa.common.domain

inline fun <T> T.applyIf(condition: Boolean, block: T.() -> Unit): T = apply {
    if (condition) {
        block()
    }
}