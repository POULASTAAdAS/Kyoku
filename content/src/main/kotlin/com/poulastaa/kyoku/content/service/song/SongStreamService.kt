package com.poulastaa.kyoku.content.service.song

import com.poulastaa.kyoku.content.database.repository.SongDataSource
import com.poulastaa.kyoku.content.utils.Email
import com.poulastaa.kyoku.content.utils.SongId
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.security.MessageDigest
import java.time.Instant
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * Builds short-lived signed HLS URLs served by kyoku-media-nginx.
 *
 * URL: <base-url>/<expires>/<uid>/<token>/master/<song_dir>/<file>
 * token = base64url(HMAC-SHA256(secret, "<expires>:<uid>:/master/<song_dir>/")), no padding.
 *
 * Must stay byte-for-byte identical to kyoku-docker/nginx/media/njs/media_auth.js.
 */
@Service
class SongStreamService(
    private val song: SongDataSource,
    @param:Value("\${media.base-url}")
    private val mediaBaseUrl: String,
    @param:Value("\${media.token.secret}")
    private val mediaTokenSecret: String,
    @param:Value("\${media.token.ttl-seconds:900}")
    private val mediaTokenTtlSeconds: Long,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    data class SignedStreamUrl(
        val url: String,
        val expiresAtSec: Long,
    )

    class SongNotFoundException(songId: SongId) : RuntimeException("Song $songId not found")

    fun getStreamUrl(songId: SongId, email: Email): SignedStreamUrl {
        val relPath = song.findById(songId).orElseThrow { SongNotFoundException(songId) }.url

        val songDir = MASTER_PLAYLIST_PATTERN.matchEntire(relPath)?.groupValues?.get(1)
            ?: run {
                logger.error("Song {} has malformed master_playlist path: {}", songId, relPath)
                throw IllegalStateException("Malformed master_playlist path for song $songId")
            }

        val uid = sha256Hex(email.lowercase()).substring(0, UID_LENGTH)
        val expires = Instant.now().epochSecond + mediaTokenTtlSeconds
        val token = hmacSha256Base64Url("$expires:$uid:/master/$songDir/")

        return SignedStreamUrl(
            url = "${mediaBaseUrl.removeSuffix("/")}/$expires/$uid/$token/$relPath",
            expiresAtSec = expires
        )
    }

    private fun sha256Hex(value: String) = MessageDigest.getInstance("SHA-256")
        .digest(value.toByteArray(Charsets.UTF_8))
        .joinToString("") { "%02x".format(it) }

    private fun hmacSha256Base64Url(data: String): String {
        val mac = Mac.getInstance(HMAC_ALGORITHM).apply {
            init(SecretKeySpec(mediaTokenSecret.toByteArray(Charsets.UTF_8), HMAC_ALGORITHM))
        }

        return Base64.getUrlEncoder().withoutPadding().encodeToString(mac.doFinal(data.toByteArray(Charsets.UTF_8)))
    }

    private companion object {
        const val HMAC_ALGORITHM = "HmacSHA256"
        const val UID_LENGTH = 16

        // master/<song_dir>/<file>.m3u8
        val MASTER_PLAYLIST_PATTERN = Regex("^master/([^/]+)/[^/]+\\.m3u8$")
    }
}
