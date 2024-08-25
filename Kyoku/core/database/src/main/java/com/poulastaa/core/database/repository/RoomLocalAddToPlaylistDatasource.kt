package com.poulastaa.core.database.repository

import com.poulastaa.core.database.dao.AddToPlaylistDao
import com.poulastaa.core.database.dao.CommonDao
import com.poulastaa.core.database.entity.FavouriteEntity
import com.poulastaa.core.database.entity.relation.SongPlaylistRelationEntity
import com.poulastaa.core.database.mapper.toPlaylistEntity
import com.poulastaa.core.database.mapper.toSavedPlaylist
import com.poulastaa.core.database.mapper.toSongEntity
import com.poulastaa.core.domain.model.PlaylistData
import com.poulastaa.core.domain.model.PrevSavedPlaylist
import com.poulastaa.core.domain.repository.add_to_playlist.LocalAddToPlaylistDatasource
import com.poulastaa.core.domain.repository.add_to_playlist.PRESENT
import com.poulastaa.core.domain.repository.add_to_playlist.SIZE
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
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
        addToPlaylistDao.getAllSavedPlaylist().first().groupBy { entry -> entry.id }
            .map { map ->
                val size = map.value.mapNotNull { it.coverImage }.count()
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

    override suspend fun checkIfSongInDatabase(songId: Long): Boolean =
        addToPlaylistDao.checkIfSongInDatabase(songId) != null

    override suspend fun editPlaylist(
        songId: Long,
        playlistIdList: Map<Long, Boolean>,
    ) {
        coroutineScope {
            playlistIdList.map { (playlistId, opp) ->
                async {
                    when (opp) {
                        true -> {
                            val entry = SongPlaylistRelationEntity(songId, playlistId)
                            commonDao.insertSongPlaylistRelation(entry)
                        }

                        false -> {
                            val entry = SongPlaylistRelationEntity(songId, playlistId)
                            commonDao.deleteSongPlaylistRelation(entry)
                        }
                    }
                }
            }.awaitAll()
        }
    }

    override suspend fun addSongToFavourite(songId: Long) {
        val entry = FavouriteEntity(songId)
        commonDao.insertOneIntoFavourite(entry)
    }

    override suspend fun removeSongFromFavourite(songId: Long) {
        val entry = FavouriteEntity(songId)
        commonDao.deleteSongFromFavourite(entry)
    }

    override suspend fun createPlaylist(data: PlaylistData) {
        val entry = data.toPlaylistEntity()
        val songEntry = data.listOfSong.map { it.toSongEntity() }

        coroutineScope {
            val song = async { commonDao.insertSongs(songEntry) }
            val playlist = async { commonDao.insertPlaylist(entry) }

            song.await()
            playlist.await()

            data.listOfSong.map {
                SongPlaylistRelationEntity(it.id, entry.id)
            }.let {
                commonDao.insertSongPlaylistRelations(it)
            }
        }
    }
}