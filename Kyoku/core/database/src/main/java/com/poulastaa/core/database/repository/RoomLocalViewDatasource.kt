package com.poulastaa.core.database.repository

import com.poulastaa.core.ViewData
import com.poulastaa.core.database.dao.CommonDao
import com.poulastaa.core.database.dao.LibraryDao
import com.poulastaa.core.database.dao.ViewDao
import com.poulastaa.core.database.mapper.toPlaylistSong
import com.poulastaa.core.database.mapper.toSongEntity
import com.poulastaa.core.database.mapper.toViewData
import com.poulastaa.core.domain.AlbumData
import com.poulastaa.core.domain.model.PlaylistData
import com.poulastaa.core.domain.model.PlaylistSong
import com.poulastaa.core.domain.model.Song
import com.poulastaa.core.domain.view.LocalViewDatasource
import javax.inject.Inject

class RoomLocalViewDatasource @Inject constructor(
    private val commonDao: CommonDao,
    private val libraryDao: LibraryDao,
    private val viewDao: ViewDao
) : LocalViewDatasource {
    override suspend fun getPlaylistOnId(id: Long): ViewData =
        viewDao.getPlaylistOnId(id).groupBy { it.playlistId }
            .map {
                it.value.toViewData(it.key)
            }.firstOrNull() ?: ViewData()

    override suspend fun getAlbumOnId(id: Long): ViewData =
        viewDao.getAlbumOnId(id).groupBy { it.playlistId }
            .map { it.value.toViewData(it.key) }.firstOrNull() ?: ViewData()

    override suspend fun getSongIdList(
        type: LocalViewDatasource.ReqType
    ): List<Long> = when (type) {
        LocalViewDatasource.ReqType.DAY_TYPE -> viewDao.getDayTypeMixSongIds()
        LocalViewDatasource.ReqType.OLD_MIX_SONG -> viewDao.getOldMixSongIds()
        LocalViewDatasource.ReqType.ARTIST_MIX -> viewDao.getFevArtistMixSongIds()
        LocalViewDatasource.ReqType.POPULAR_MIX -> viewDao.getPopularSongMixSongIds()
        LocalViewDatasource.ReqType.FEV -> viewDao.getFevSongIds()
    }

    override suspend fun getPrevSongIdList(type: LocalViewDatasource.ReqType): List<Long> =
        when (type) {
            LocalViewDatasource.ReqType.DAY_TYPE -> viewDao.getPrevDayTypeMixSongIds()
            LocalViewDatasource.ReqType.OLD_MIX_SONG -> viewDao.getPrevOldMixSongIds()
            LocalViewDatasource.ReqType.ARTIST_MIX -> viewDao.getPrevFevArtistMixSongIds()
            LocalViewDatasource.ReqType.POPULAR_MIX -> viewDao.getPrevPopularSongMixSongIds()
            LocalViewDatasource.ReqType.FEV -> emptyList()
        }

    override suspend fun getSongOnIdList(list: List<Long>): List<PlaylistSong> =
        viewDao.getSongOnIdList(list).map {
            it.toPlaylistSong()
        }

    override suspend fun getFevSongIdList(): List<Long> = viewDao.getFevSongIds()

    override suspend fun getOldMix(): List<PlaylistSong> {
        return listOf()
    }

    override suspend fun getArtistMix(): List<PlaylistSong> {
        return listOf()
    }

    override suspend fun getPopularMix(): List<PlaylistSong> {
        return listOf()
    }

    override suspend fun saveSongs(list: List<Song>) {
        list.map { it.toSongEntity() }.let { commonDao.insertSongs(it) }
    }

    override suspend fun savePlaylist(data: PlaylistData) {

    }

    override suspend fun saveAlbum(data: AlbumData) {

    }
}