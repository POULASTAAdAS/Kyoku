package com.poulastaa.core.domain.view

import com.poulastaa.core.ViewData
import com.poulastaa.core.domain.model.PlaylistSong

interface LocalViewDatasource {
    suspend fun getPlaylistOnId(id: Long): ViewData
    suspend fun getAlbumOnId(id: Long): ViewData
    suspend fun getFev(): List<PlaylistSong>
    suspend fun getOldMix(): List<PlaylistSong>
    suspend fun getArtistMix(): List<PlaylistSong>
    suspend fun getPopularMix(): List<PlaylistSong>
}