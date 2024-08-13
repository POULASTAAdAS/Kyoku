package com.poulastaa.core.database.repository

import com.poulastaa.core.database.dao.AddToPlaylistDao
import com.poulastaa.core.database.dao.CommonDao
import com.poulastaa.core.database.entity.FavouriteEntity
import com.poulastaa.core.database.entity.relation.SongPlaylistRelationEntity
import com.poulastaa.core.database.mapper.toSavedPlaylist
import com.poulastaa.core.domain.add_to_playlist.LocalAddToPlaylistDatasource
import com.poulastaa.core.domain.add_to_playlist.PRESENT
import com.poulastaa.core.domain.add_to_playlist.SIZE
import com.poulastaa.core.domain.model.PrevSavedPlaylist
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class RoomLocalAddToPlaylistDatasource @Inject constructor(
    private val commonDao: CommonDao,
    private val addToPlaylistDao: AddToPlaylistDao
) : LocalAddToPlaylistDatasource {
    override suspend fun checkIfSongInFev(songId: Long): Boolean =
        addToPlaylistDao.getFavouriteEntryOnSongId(songId) != null

    override suspend fun getTotalSongsInFev(): Int = addToPlaylistDao.getTotalFavouriteEntryCount()

    override suspend fun getPlaylistData(songId: Long): List<Pair<Pair<SIZE, PRESENT>, PrevSavedPlaylist>> =
        commonDao.getAllSavedPlaylist().first().groupBy { entry -> entry.id }
            .map { map ->
                val size = map.value.count()
                val present =
                    addToPlaylistDao.getSongPlaylistRelationEntryOnSongId(
                        songId = songId,
                        playlistId = map.key
                    ) != null

                Pair(
                    Pair(size, present),
                    map.toSavedPlaylist()
                )
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