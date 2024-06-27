package com.poulastaa.data.repository

import com.poulastaa.data.dao.SongDao
import com.poulastaa.data.model.PlaylistDto
import com.poulastaa.domain.repository.SetupRepository
import com.poulastaa.domain.repository.SpotifySongTitle
import com.poulastaa.domain.table.SongTable
import com.poulastaa.plugins.dbQuery
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class SetupRepositoryDatabaseImpl : SetupRepository {
    override suspend fun getSpotifyPlaylist(spotifyPayload: List<SpotifySongTitle>): PlaylistDto {

        coroutineScope {
            val result = spotifyPayload.map {
                async {
                    dbQuery {
                        SongDao.find {
                            SongTable.title like "${it}%"
                        }
                    }
                }
            }.awaitAll()
        }






        return PlaylistDto()
    }

}