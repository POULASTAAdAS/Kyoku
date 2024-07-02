package com.poulastaa.domain.repository

import com.poulastaa.data.model.ArtistDto
import com.poulastaa.data.model.GenreDto
import com.poulastaa.domain.model.PlaylistResult

typealias SpotifySongTitle = String

interface SetupRepository {
    suspend fun getSpotifyPlaylist(
        spotifyPayload: List<SpotifySongTitle>,
    ): PlaylistResult

    suspend fun getGenre(
        sentGenreIdList: List<Int>,
    ): List<GenreDto>

    suspend fun getArtist(
        sentArtistIdList: List<Long>,
    ): List<ArtistDto>
}