package com.poulastaa.core.domain.repository.artist

import com.poulastaa.core.domain.model.Artist
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.EmptyResult
import com.poulastaa.core.domain.utils.Result

interface ArtistRepository {
    suspend fun getArtist(sentList: List<Long>): Result<List<Artist>, DataError.Network>
    suspend fun insertArtists(entrys: List<Artist>): EmptyResult<DataError.Network>
}