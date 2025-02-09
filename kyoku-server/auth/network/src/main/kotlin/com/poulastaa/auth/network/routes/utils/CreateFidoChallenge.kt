package com.poulastaa.auth.network.routes.utils

import java.security.SecureRandom
import java.util.*

internal fun generateFidoChallenge(): String {
    val secureRandom = SecureRandom()
    val challengeBytes = ByteArray(64)
    secureRandom.nextBytes(challengeBytes)
    return challengeBytes.b64Encode()
}

private fun ByteArray.b64Encode(): String {
    return Base64.getUrlEncoder().encodeToString(this)
}