package com.poulastaa.core.database.repository

import com.poulastaa.core.database.dao.CommonDao
import com.poulastaa.core.database.dao.ViewDao
import com.poulastaa.core.database.entity.DayTypeSongEntity
import com.poulastaa.core.database.entity.FavouriteArtistMixEntity
import com.poulastaa.core.database.entity.FavouriteEntity
import com.poulastaa.core.database.entity.PopularSongFromYourTimeEntity
import com.poulastaa.core.database.entity.PopularSongMixEntity
import com.poulastaa.core.database.entity.relation.SongPlaylistRelationEntity
import com.poulastaa.core.database.mapper.toAlbumEntity
import com.poulastaa.core.database.mapper.toPlaylistEntity
import com.poulastaa.core.database.mapper.toPlaylistSong
import com.poulastaa.core.database.mapper.toSongEntity
import com.poulastaa.core.database.mapper.toViewData
import com.poulastaa.core.domain.model.AlbumWithSong
import com.poulastaa.core.domain.model.PlaylistData
import com.poulastaa.core.domain.model.PlaylistSong
import com.poulastaa.core.domain.model.Song
import com.poulastaa.core.domain.model.ViewData
import com.poulastaa.core.domain.repository.view.LocalViewDatasource
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class RoomLocalViewDatasource @Inject constructor(
    private val commonDao: CommonDao,
    private val viewDao: ViewDao
) : LocalViewDatasource {
    override suspend fun getPlaylistOnId(id: Long): ViewData =
        viewDao.getPlaylistOnId(id).groupBy { it.playlistId }
            .map {
                it.value.toViewData(it.key)
            }.firstOrNull() ?: ViewData()

    override suspend fun getAlbumOnId(id: Long): ViewData =
        viewDao.getAlbumForViewData(id).groupBy { it.playlistId }
            .map { it.value.toViewData(it.key) }.firstOrNull() ?: ViewData()

    override suspend fun isAlbumOnLibrary(id: Long): Boolean = viewDao.getAlbumOnId(id) != null

    override suspend fun isSongInFavourite(songId: Long): Boolean =
        commonDao.isSongInFavourite(songId) != null

    override suspend fun getSongIdList(type: LocalViewDatasource.ReqType): List<Long> =
        when (type) {
            LocalViewDatasource.ReqType.DAY_TYPE -> viewDao.getDayTypeMixSongIds()
            LocalViewDatasource.ReqType.OLD_MIX_SONG -> viewDao.getOldMixSongIds()
            LocalViewDatasource.ReqType.ARTIST_MIX -> viewDao.getFevArtistMixSongIds()
            LocalViewDatasource.ReqType.POPULAR_MIX -> viewDao.getPopularSongMixSongIds()
            LocalViewDatasource.ReqType.FEV -> commonDao.getFevSongIds()
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

    override suspend fun getFevSongIdList(): List<Long> = commonDao.getFevSongIds()

    override suspend fun getOldMix(): List<PlaylistSong> {
        TODO("not implemented")
    }

    override suspend fun getArtistMix(): List<PlaylistSong> {
        TODO("not implemented")
    }

    override suspend fun getPopularMix(): List<PlaylistSong> {
        TODO("not implemented")
    }

    override suspend fun insertSongs(list: List<Song>, type: LocalViewDatasource.ReqType?) {
        list.map { it.toSongEntity() }.let { commonDao.insertSongs(it) }
        val idList = list.map { it.id }

        when (type) {
            null -> Unit

            LocalViewDatasource.ReqType.DAY_TYPE -> {
                val entrys = idList.map { DayTypeSongEntity(it) }
                commonDao.insertIntoDayType(entrys)
            }

            LocalViewDatasource.ReqType.OLD_MIX_SONG -> {
                val entrys = idList.map { PopularSongFromYourTimeEntity(it) }
                commonDao.insertIntoOldMix(entrys)
            }

            LocalViewDatasource.ReqType.ARTIST_MIX -> {
                val entrys = idList.map { FavouriteArtistMixEntity(it) }
                commonDao.insertIntoArtistMix(entrys)
            }

            LocalViewDatasource.ReqType.POPULAR_MIX -> {
                val entrys = idList.map { PopularSongMixEntity(it) }
                commonDao.insertIntoPopularSongMix(entrys)
            }

            LocalViewDatasource.ReqType.FEV -> {
                val entrys = idList.map { FavouriteEntity(it) }
                commonDao.insertMultipleIntoFavourite(entrys)
            }
        }
    }

    override suspend fun savePlaylist(data: PlaylistData) {
        if (data.id == -1L) return

        coroutineScope {
            val song = async {
                data.listOfSong.map { it.toSongEntity() }.let {
                    commonDao.insertSongs(it)
                }
            }

            val playlist = async {
                data.toPlaylistEntity().let {
                    commonDao.insertPlaylist(it)
                }
            }

            song.await()
            playlist.await()

            data.listOfSong.map {
                SongPlaylistRelationEntity(
                    songId = it.id,
                    playlistId = data.id
                )
            }.let {
                commonDao.insertSongPlaylistRelations(it)
            }
        }
    }

    override suspend fun saveAlbum(data: AlbumWithSong) {
        coroutineScope {
            async {
                data.listOfSong.map { it.toSongEntity() }.let {
                    commonDao.insertSongs(it)
                }
            }.await()

            commonDao.insertAlbum(data.album.toAlbumEntity())
        }
    }
}