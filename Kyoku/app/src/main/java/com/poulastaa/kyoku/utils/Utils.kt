package com.poulastaa.kyoku.utils

fun Char.isUserName(): Boolean =
    if (this == '_') true
    else this.isLetterOrDigit()