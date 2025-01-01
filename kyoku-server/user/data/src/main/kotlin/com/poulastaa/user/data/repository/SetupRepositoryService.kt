package com.poulastaa.user.data.repository

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
}