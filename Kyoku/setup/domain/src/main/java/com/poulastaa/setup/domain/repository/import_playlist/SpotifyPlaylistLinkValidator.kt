package com.poulastaa.setup.domain.repository.import_playlist

interface SpotifyPlaylistLinkValidator {
    fun validate(link: String): String?
}