package com.poulastaa.core.database.repository.setup

import com.poulastaa.core.domain.model.DBUserDto
import com.poulastaa.core.domain.model.PlaylistFullDto
import com.poulastaa.core.domain.model.UserType
import com.poulastaa.core.domain.repository.LocalCoreDatasource
import com.poulastaa.core.domain.repository.setup.LocalSetupCacheDatasource
import com.poulastaa.core.domain.repository.setup.LocalSetupDatasource

class ExposedSetupDatasource(
    private val coreDB: LocalCoreDatasource,
    private val cache: LocalSetupCacheDatasource,
) : LocalSetupDatasource {
    override suspend fun getUserByEmail(
        email: String,
        userType: UserType,
    ): DBUserDto? = coreDB.getUserByEmail(email, userType)

    override suspend fun createPlaylistFromSpotifyPlaylist(
        user: DBUserDto,
        spotifySongTitle: List<String>,
    ): PlaylistFullDto {
        TODO("Not yet implemented")
    }
}