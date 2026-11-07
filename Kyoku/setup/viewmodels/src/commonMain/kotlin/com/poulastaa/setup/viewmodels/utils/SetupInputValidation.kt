package com.poulastaa.setup.viewmodels.utils

/**
 * Spotify playlist ids are 22 base62 characters.
 *
 * Supported inputs:
 * - `https://open.spotify.com/playlist/{id}?si=...`
 * - `https://open.spotify.com/intl-xx/playlist/{id}`
 * - `https://open.spotify.com/embed/playlist/{id}`
 * - `spotify:playlist:{id}`
 */
private val SPOTIFY_PLAYLIST_URL_REGEX = Regex(
    "^(?:https?://)?open\\.spotify\\.com/(?:intl-[A-Za-z-]+/)?(?:embed/)?playlist/([A-Za-z0-9]{22})(?:[/?#].*)?$"
)
private val SPOTIFY_PLAYLIST_URI_REGEX = Regex("^spotify:playlist:([A-Za-z0-9]{22})$")

internal fun String.normalizedLink() = trim()

/**
 * Returns the Spotify playlist id when this is a valid Spotify playlist link, otherwise null.
 */
internal fun String.spotifyPlaylistIdOrNull(): String? {
    val link = normalizedLink()

    return (SPOTIFY_PLAYLIST_URL_REGEX.matchEntire(link)
        ?: SPOTIFY_PLAYLIST_URI_REGEX.matchEntire(link))
        ?.groupValues
        ?.getOrNull(1)
}
