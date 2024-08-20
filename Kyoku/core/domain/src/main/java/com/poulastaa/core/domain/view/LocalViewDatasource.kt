package com.poulastaa.core.domain.view

import com.poulastaa.core.ViewData
import com.poulastaa.core.domain.model.PlaylistSong
import com.poulastaa.core.domain.model.Song

interface LocalViewDatasource {
    suspend fun getPlaylistOnId(id: Long): ViewData
    suspend fun getAlbumOnId(id: Long): ViewData

    suspend fun getSongIdList(type: ReqType): List<Long>
    suspend fun getSongOnIdList(list: List<Long>): List<PlaylistSong>

    suspend fun getPrevSongIdList(type: ReqType): List<Long>

    suspend fun getFevSongIdList(): List<Long>
    suspend fun getOldMix(): List<PlaylistSong>
    suspend fun getArtistMix(): List<PlaylistSong>
    suspend fun getPopularMix(): List<PlaylistSong>

    suspend fun saveSongs(list: List<Song>)

    enum class ReqType {
        DAY_TYPE,
        OLD_MIX_SONG,
        ARTIST_MIX,
        POPULAR_MIX,
        FEV
    }
}