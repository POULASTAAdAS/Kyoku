package com.poulastaa.core.domain.view

import com.poulastaa.core.domain.ViewSong

interface RemoteViewDatasource {
    suspend fun getPlaylistOnId(id: Long): List<ViewSong>
    suspend fun getAlbumOnId(id: Long): List<ViewSong>
    suspend fun getFev(): List<ViewSong>
    suspend fun getOldMix(): List<ViewSong>
    suspend fun getArtistMix(): List<ViewSong>
    suspend fun getPopularMix(): List<ViewSong>
}