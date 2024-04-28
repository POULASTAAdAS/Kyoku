package com.poulastaa.utils

import com.poulastaa.data.model.EndPoints
import com.poulastaa.utils.Constants.ACCESS_TOKEN_CLAIM_KEY
import com.poulastaa.utils.Constants.COVER_IMAGE_ROOT_DIR
import com.poulastaa.utils.Constants.MASTER_PLAYLIST_ROOT_DIR
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import java.security.SecureRandom
import java.util.*

fun ApplicationCall.getClaimFromPayload(): String? =
    this.authentication.principal<JWTPrincipal>()?.payload?.getClaim(ACCESS_TOKEN_CLAIM_KEY)?.asString()


fun generateFidoChallenge(): String {
    val secureRandom = SecureRandom()
    val challengeBytes = ByteArray(64)
    secureRandom.nextBytes(challengeBytes)
    return challengeBytes.b64Encode()
}

fun ByteArray.b64Encode(): String = Base64.getUrlEncoder().encodeToString(this)

fun constructProfileUrl(): String = "${Constants.SERVICE_URL}${EndPoints.ProfilePic.route}"

fun String.constructCoverPhotoUrl(): String = "${Constants.SERVICE_URL}${EndPoints.CoverImage.route}?coverImage=${
    this.replace(COVER_IMAGE_ROOT_DIR, "")
}"

fun String.constructMasterPlaylistUrl(): String = "${Constants.SERVICE_URL}${EndPoints.PlaySongMaster.route}?master=${
    this.replace(MASTER_PLAYLIST_ROOT_DIR, "")
}"