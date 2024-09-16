package com.poulastaa.core.database.repository

import com.poulastaa.core.database.dao.CommonDao
import com.poulastaa.core.database.dao.ViewDao
import com.poulastaa.core.database.entity.relation.SongPlaylistRelationEntity
import com.poulastaa.core.database.mapper.toPlaylistSong
import com.poulastaa.core.domain.model.PlaylistSong
import com.poulastaa.core.domain.model.ViewEditType
import com.poulastaa.core.domain.repository.view_edit.LocalViewEditDatasource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class RoomLocalViewEditDatasource @Inject constructor(
    private val commonDao: CommonDao,
    private val viewDao: ViewDao,
) : LocalViewEditDatasource {
    override fun getSongs(
        playlistId: Long,
        type: ViewEditType,
    ): Flow<List<PlaylistSong>> = when (type) {
        ViewEditType.PLAYLIST -> commonDao.getPlaylistSongsAsFlow(playlistId).map {
            it.map { dao ->
                dao.toPlaylistSong()
            }
        }

        ViewEditType.FEV -> commonDao.getFavouriteSongsAsFlow().map {
            it.map { dao ->
                dao.toPlaylistSong()
            }
        }
    }

    override suspend fun deleteSong(playlistId: Long, songId: Long) {
        commonDao.deleteSongPlaylistRelation(
            entry = SongPlaylistRelationEntity(
                songId = songId,
                playlistId = playlistId
            )
        )
    }

    override suspend fun addSong(playlistId: Long, songId: Long) {
        commonDao.insertSongPlaylistRelation(
            entry = SongPlaylistRelationEntity(
                songId = songId,
                playlistId = playlistId
            )
        )
    }

    override suspend fun getAllSongIds(playlistId: Long): List<Long> =
        viewDao.getPlaylistOnId(playlistId).map { it.songId }
}