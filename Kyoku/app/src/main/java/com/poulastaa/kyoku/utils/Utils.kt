package com.poulastaa.kyoku.utils

import java.net.CookieManager
import java.util.Base64
import kotlin.random.Random

fun Char.isUserName(): Boolean =
    if (this == '_') true
    else this.isLetterOrDigit()

fun String.b64Decode(): ByteArray = Base64.getUrlDecoder().decode(this)

fun CookieManager.extractTokenOrCookie(): String =
    this.cookieStore.cookies[0].toString()

fun String.validateSpotifyLink(): Boolean {
    return (this.startsWith("https://open.spotify.com/playlist/") && this.contains("?si="))
}

fun String.toSpotifyPlaylistId(): String =
    this.removePrefix("https://open.spotify.com/playlist/").split("?si=")[0]

fun generatePlaylistName(): String = "Playlist #${Random.nextLong(1, 10_000)}"
