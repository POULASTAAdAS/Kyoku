package com.poulastaa.domain.repository

import com.poulastaa.data.model.PlaylistDto
import com.poulastaa.data.model.SuggestArtistDao
import com.poulastaa.data.model.SuggestGenreDto
import com.poulastaa.domain.model.ReqUserPayload

interface ServiceRepository {
    suspend fun getSpotifyPlaylist(
        userPayload: ReqUserPayload,
        spotifyPayload: List<SpotifySongTitle>,
    ): PlaylistDto

    suspend fun updateBDate(
        userPayload: ReqUserPayload,
        date: Long,
    ): Boolean

    suspend fun getGenre(
        userPayload: ReqUserPayload,
        genreIds: List<Int>,
    ): SuggestGenreDto

    suspend fun storeGenre(
        userPayload: ReqUserPayload,
        genreIds: List<Int>,
    ): Boolean

    suspend fun getArtist(
        userPayload: ReqUserPayload,
        artistIds: List<Long>,
    ): SuggestArtistDao
}