package com.poulastaa.main.domain.repository.work

import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.EmptyResult

interface WorkRepository {
    suspend fun syncSavedAlbums(): EmptyResult<DataError.Network>
    suspend fun syncSavedPlaylist(): EmptyResult<DataError.Network>
    suspend fun syncSavedArtist(): EmptyResult<DataError.Network>
    suspend fun syncSavedFavourite(): EmptyResult<DataError.Network>
}