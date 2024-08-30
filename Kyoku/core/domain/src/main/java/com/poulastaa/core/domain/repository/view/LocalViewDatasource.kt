package com.poulastaa.core.domain.repository.view

import com.poulastaa.core.domain.model.ViewData
import com.poulastaa.core.domain.model.AlbumWithSong
import com.poulastaa.core.domain.model.PlaylistData
import com.poulastaa.core.domain.model.PlaylistSong
import com.poulastaa.core.domain.model.Song

interface LocalViewDatasource {
    suspend fun getPlaylistOnId(id: Long): ViewData
    suspend fun getAlbumOnId(id: Long): ViewData

    suspend fun isAlbumOnLibrary(id: Long): Boolean

    suspend fun isSongInFavourite(songId: Long): Boolean

    suspend fun getSongIdList(type: ReqType): List<Long>
    suspend fun getSongOnIdList(list: List<Long>): List<PlaylistSong>

    suspend fun getPrevSongIdList(type: ReqType): List<Long>

    suspend fun getFevSongIdList(): List<Long>
    suspend fun getOldMix(): List<PlaylistSong>
    suspend fun getArtistMix(): List<PlaylistSong>
    suspend fun getPopularMix(): List<PlaylistSong>

    suspend fun insertSongs(list: List<Song>, type: ReqType? = null)
    suspend fun savePlaylist(data: PlaylistData)
    suspend fun saveAlbum(data: AlbumWithSong)

    enum class ReqType {
        DAY_TYPE,
        OLD_MIX_SONG,
        ARTIST_MIX,
        POPULAR_MIX,
        FEV
    }
}