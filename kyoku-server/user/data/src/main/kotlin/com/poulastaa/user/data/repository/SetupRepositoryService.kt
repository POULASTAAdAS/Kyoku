package com.poulastaa.user.data.repository

import com.poulastaa.core.domain.model.DtoGenre
import com.poulastaa.core.domain.model.DtoPlaylistFull
import com.poulastaa.core.domain.model.ReqUserPayload
import com.poulastaa.core.domain.repository.setup.LocalSetupDatasource
import com.poulastaa.user.domain.repository.SetupRepository
import com.poulastaa.user.domain.repository.SpotifySongTitle

class SetupRepositoryService(
    private val db: LocalSetupDatasource,
) : SetupRepository {
    override suspend fun getSpotifyPlaylist(
        userPayload: ReqUserPayload,
        spotifyPayload: List<SpotifySongTitle>,
    ): DtoPlaylistFull? {
        val user = db.getUserByEmail(userPayload.email, userPayload.userType) ?: return null

        return db.createPlaylistFromSpotifyPlaylist(user, spotifyPayload)
    }

    override suspend fun setBDate(
        userPayload: ReqUserPayload,
        bDate: String,
    ): Boolean {
        val user = db.getUserByEmail(userPayload.email, userPayload.userType) ?: return false

        return db.updateBDate(user, bDate)
    }

    override suspend fun getGenre(genreIds: List<Int>): List<DtoGenre> = db.getPagingGenre(genreIds)
}