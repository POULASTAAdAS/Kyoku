package com.poulastaa.core.domain.artist

import com.poulastaa.core.domain.model.Artist
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.EmptyResult
import com.poulastaa.core.domain.utils.Result

interface RemoteArtistDataSource {
    suspend fun getArtist(sentList: List<Long>): Result<List<Artist>, DataError.Network>
    suspend fun storeArtist(idList: List<Long>): EmptyResult<DataError.Network>
}