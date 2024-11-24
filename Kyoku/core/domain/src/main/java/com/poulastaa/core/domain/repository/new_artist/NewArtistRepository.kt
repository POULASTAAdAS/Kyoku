package com.poulastaa.core.domain.repository.new_artist

import androidx.paging.PagingData
import com.poulastaa.core.domain.model.Artist
import com.poulastaa.core.domain.model.ArtistPagingType
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.EmptyResult
import kotlinx.coroutines.flow.Flow

interface NewArtistRepository {
    fun getPagingArtist(
        query: String,
        type: ArtistPagingType,
    ): Flow<PagingData<Artist>>

    suspend fun saveArtist(list: List<Long>): EmptyResult<DataError.Network>
}