package com.poulastaa.setup.data.repository

import com.poulastaa.setup.domain.repository.import_playlist.SpotifyPlaylistLinkValidator


class UseCaseValidatePlaylistLink : SpotifyPlaylistLinkValidator {
    override fun validate(link: String): String? =
        if (link.startsWith(SPOTIFY_URL) && link.contains("?si="))
            link.removePrefix(SPOTIFY_URL).split("?si=")[0]
        else null

    private companion object {
        const val SPOTIFY_URL = "https://open.spotify.com/playlist/"
    }
}