package com.poulastaa.core.database.repository

import com.poulastaa.core.database.dao.CommonDao
import com.poulastaa.core.database.dao.WorkDao
import com.poulastaa.core.database.mapper.toAlbumEntity
import com.poulastaa.core.database.mapper.toArtistEntity
import com.poulastaa.core.database.mapper.toPlaylistEntity
import com.poulastaa.core.database.mapper.toSongEntity
import com.poulastaa.core.domain.model.AlbumWithSong
import com.poulastaa.core.domain.model.Artist
import com.poulastaa.core.domain.model.PlaylistWithSong
import com.poulastaa.core.domain.repository.work.LocalWorkDatasource
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class RoomLocalWorkDatasource @Inject constructor(
    private val commonDao: CommonDao,
    private val workDao: WorkDao
) : LocalWorkDatasource {
    override suspend fun getAllAlbumId(): List<Long> = workDao.getAllSavedAlbumId()

    override suspend fun saveAlbums(entry: List<AlbumWithSong>) {
        coroutineScope {
            async {
                entry.map { it.listOfSong }
                    .flatten()
                    .map { it.toSongEntity() }.let {
                        commonDao.insertSongs(it)
                    }
            }.await()

            entry.map { it.album }.map { it.toAlbumEntity() }.let {
                commonDao.insertAlbums(it)
            }
        }
    }

    override suspend fun removeAlbums(list: List<Long>) = workDao.deleteAlbums(list)


    override suspend fun getAllPlaylistId(): List<Long> = workDao.getAllSavedPlaylistId()

    override suspend fun savePlaylists(entry: List<PlaylistWithSong>) {
        coroutineScope {
            async {
                entry.map { it.songs }
                    .flatten()
                    .map { it.toSongEntity() }.let {
                        commonDao.insertSongs(it)
                    }
            }.await()

            entry.map { it.playlist }.map { it.toPlaylistEntity() }.let {
                commonDao.insertPlaylists(it)
            }
        }
    }

    override suspend fun removePlaylist(list: List<Long>) = workDao.deletePlaylist(list)

    override suspend fun getAllArtistsId(): List<Long> = workDao.getAllSavedArtistId()

    override suspend fun saveArtists(entry: List<Artist>) {
        entry.map { it.toArtistEntity() }
            .let {
                commonDao.insertArtists(it)
            }
    }

    override suspend fun removeArtists(list: List<Long>) = workDao.deleteArtists(list)
}