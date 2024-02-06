package com.poulastaa.kyoku.utils

import java.util.Base64

fun Char.isUserName(): Boolean =
    if (this == '_') true
    else this.isLetterOrDigit()

fun String.b64Decode(): ByteArray {
    return Base64.getUrlDecoder().decode(this)
}