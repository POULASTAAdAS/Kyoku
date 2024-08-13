package com.poulastaa.core.database.repository

import com.poulastaa.core.database.dao.AddToPlaylistDao
import com.poulastaa.core.database.dao.CommonDao
import com.poulastaa.core.database.entity.FavouriteEntity
import com.poulastaa.core.database.entity.relation.SongPlaylistRelationEntity
import com.poulastaa.core.database.mapper.toSavedPlaylist
import com.poulastaa.core.domain.add_to_playlist.LocalAddToPlaylistDatasource
import com.poulastaa.core.domain.model.PrevSavedPlaylist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomLocalAddToPlaylistDatasource @Inject constructor(
    private val commonDao: CommonDao,
    private val addToPlaylistDao: AddToPlaylistDao
) : LocalAddToPlaylistDatasource {
    override suspend fun checkIfSongInFev(songId: Long): Boolean =
        addToPlaylistDao.getFavouriteEntryOnSongId(songId) != null

    override suspend fun getTotalSongsInFev(): Int = addToPlaylistDao.getTotalFavouriteEntryCount()

    override suspend fun getPlaylistData(): Flow<List<Pair<Int, PrevSavedPlaylist>>> =
        commonDao.getAllSavedPlaylist().map {
            it.groupBy { entry -> entry.id }.map { map ->
                map.value.count() to map.toSavedPlaylist()
            }
        }

    override suspend fun addSongToPlaylist(songId: Long, playlistId: Long) {
        val entry = SongPlaylistRelationEntity(songId, playlistId)
        commonDao.insertSongPlaylistRelation(entry)
    }

    override suspend fun addSongToFavourite(songId: Long) {
        val entry = FavouriteEntity(songId)
        commonDao.insertOneIntoFavourite(entry)
    }
}