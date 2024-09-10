package com.poulastaa.core.domain.repository.work

import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.EmptyResult

interface WorkRepository { // todo update playlist song
    suspend fun getUpdatedAlbums(): EmptyResult<DataError.Network>
    suspend fun getUpdatedPlaylists(): EmptyResult<DataError.Network>
    suspend fun getUpdatedArtists(): EmptyResult<DataError.Network>
    suspend fun getUpdatedFavourite(): EmptyResult<DataError.Network>
}