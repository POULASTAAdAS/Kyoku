package com.poulastaa.core.domain.view

import com.poulastaa.core.ViewData
import com.poulastaa.core.domain.model.PlaylistSong
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.Result

interface ViewRepository {
    suspend fun getPlaylistOnId(id: Long): Result<ViewData, DataError.Network>
    suspend fun getAlbumOnId(id: Long): Result<ViewData, DataError.Network>
    suspend fun getFev(): Result<List<PlaylistSong>, DataError.Network>
    suspend fun getOldMix(): Result<List<PlaylistSong>, DataError.Network>
    suspend fun getArtistMix(): Result<List<PlaylistSong>, DataError.Network>
    suspend fun getPopularMix(): Result<List<PlaylistSong>, DataError.Network>
}