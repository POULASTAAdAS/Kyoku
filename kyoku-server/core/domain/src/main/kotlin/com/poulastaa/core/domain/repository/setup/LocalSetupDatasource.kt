package com.poulastaa.core.domain.repository.setup

import com.poulastaa.core.domain.model.DtoDBUser
import com.poulastaa.core.domain.model.DtoPlaylistFull
import com.poulastaa.core.domain.model.UserType

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
}