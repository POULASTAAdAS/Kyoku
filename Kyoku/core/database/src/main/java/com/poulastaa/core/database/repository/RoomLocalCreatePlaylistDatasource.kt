package com.poulastaa.core.database.repository

import com.poulastaa.core.database.dao.CommonDao
import com.poulastaa.core.database.entity.relation.SongPlaylistRelationEntity
import com.poulastaa.core.database.mapper.toSongEntity
import com.poulastaa.core.domain.model.Song
import com.poulastaa.core.domain.repository.create_playlist.LocalCreatePlaylistDatasource
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject


class RoomLocalCreatePlaylistDatasource @Inject constructor(
    private val commonDao: CommonDao
) : LocalCreatePlaylistDatasource {
    override suspend fun saveSong(song: Song, playlistId: Long) {
        coroutineScope {
            async { commonDao.insertSong(song.toSongEntity()) }.await()
            commonDao.insertSongPlaylistRelation(
                SongPlaylistRelationEntity(
                    songId = song.id,
                    playlistId = playlistId
                )
            )
        }
    }
}