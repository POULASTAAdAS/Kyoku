package com.poulastaa.core.domain.view

import com.poulastaa.core.ViewData
import com.poulastaa.core.domain.AlbumData
import com.poulastaa.core.domain.model.PlaylistData
import com.poulastaa.core.domain.model.PlaylistSong
import com.poulastaa.core.domain.model.Song


interface RemoteViewDatasource {
    suspend fun getPlaylistOnId(id: Long): PlaylistData
    suspend fun getAlbumOnId(id: Long): AlbumData

    suspend fun getFev(): List<Song>
    suspend fun getOldMix(prevList: List<Long>): List<Song>
    suspend fun getArtistMix(prevList: List<Long>): List<Song>
    suspend fun getPopularMix(prevList: List<Long>): List<Song>

    suspend fun getSongOnIdList(list: List<Long>): List<Song>
}