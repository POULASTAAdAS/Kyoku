package com.poulastaa.core.domain.repository.setup

import com.poulastaa.core.domain.model.DBUserDto
import com.poulastaa.core.domain.model.PlaylistFullDto
import com.poulastaa.core.domain.model.UserType

interface LocalSetupDatasource {
    suspend fun getUserByEmail(email: String, userType: UserType): DBUserDto?

    suspend fun createPlaylistFromSpotifyPlaylist(
        user: DBUserDto,
        spotifySongTitle: List<String>,
    ): PlaylistFullDto
}