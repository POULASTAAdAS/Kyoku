package com.poulastaa.core.domain.repository.view

import com.poulastaa.core.domain.model.ViewData
import com.poulastaa.core.domain.model.PlaylistSong
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.EmptyResult
import com.poulastaa.core.domain.utils.Result

interface ViewRepository {
    suspend fun getPlaylistOnId(id: Long): Result<ViewData, DataError.Network>
    suspend fun getAlbumOnId(id: Long): Result<ViewData, DataError.Network>

    suspend fun isSavedAlbum(id: Long): Boolean

    suspend fun isSongInFavourite(songId: Long): Boolean

    suspend fun getFev(): Result<List<PlaylistSong>, DataError.Network>
    suspend fun getOldMix(): Result<List<PlaylistSong>, DataError.Network>
    suspend fun getArtistMix(): Result<List<PlaylistSong>, DataError.Network>
    suspend fun getPopularMix(): Result<List<PlaylistSong>, DataError.Network>
    suspend fun addSongToFavourite(songId: Long): EmptyResult<DataError.Network>
}