package com.poulastaa.core.domain.repository.setup

import com.poulastaa.core.domain.model.*
import com.poulastaa.core.domain.repository.ArtistId
import com.poulastaa.core.domain.repository.GenreId

interface LocalSetupDatasource {
    suspend fun getUserByEmail(email: String, userType: UserType): DtoDBUser?

    suspend fun createPlaylistFromSpotifyPlaylist(
        user: DtoDBUser,
        spotifySongTitle: List<String>,
    ): DtoPlaylistFull

    suspend fun updateBDate(
        user: DtoDBUser,
        bDate: String,
    ): Boolean

    suspend fun getPagingGenre(
        page: Int,
        size: Int,
        query: String,
    ): List<DtoGenre>

    suspend fun upsertGenre(
        user: DtoDBUser,
        data: DtoUpsert<GenreId>,
    ): List<DtoGenre>

    suspend fun getPagingArtist(
        page: Int,
        size: Int,
        query: String,
        userId: Long,
    ): List<DtoPrevArtist>

    suspend fun upsertArtist(
        user: DtoDBUser,
        data: DtoUpsert<ArtistId>,
    ): List<DtoArtist>
}