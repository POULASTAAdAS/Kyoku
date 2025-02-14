package com.poulastaa.setup.domain.repository.set_artist

import androidx.paging.PagingData
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.EmptyResult
import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.core.domain.model.DtoPrevArtist
import kotlinx.coroutines.flow.Flow

interface SetArtistRepository {
    fun suggestArtist(query: String): Flow<PagingData<DtoPrevArtist>>
    suspend fun storeArtist(list: List<ArtistId>): EmptyResult<DataError.Network>
}