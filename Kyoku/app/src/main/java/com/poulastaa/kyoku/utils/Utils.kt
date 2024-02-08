package com.poulastaa.kyoku.utils

import java.net.CookieManager
import java.util.Base64

fun Char.isUserName(): Boolean =
    if (this == '_') true
    else this.isLetterOrDigit()

fun String.b64Decode(): ByteArray = Base64.getUrlDecoder().decode(this)

fun CookieManager.extractTokenOrCookie(): String =
    this.cookieStore.cookies[0].toString()