package com.poulastaa.core.domain.view

import com.poulastaa.core.ViewData
import com.poulastaa.core.domain.ViewSong

interface ViewRepository {
    suspend fun getPlaylistOnId(id: Long): ViewData
    suspend fun getAlbumOnId(id: Long): ViewData
    suspend fun getFev(): List<ViewSong>
    suspend fun getOldMix(): List<ViewSong>
    suspend fun getArtistMix(): List<ViewSong>
    suspend fun getPopularMix(): List<ViewSong>
}