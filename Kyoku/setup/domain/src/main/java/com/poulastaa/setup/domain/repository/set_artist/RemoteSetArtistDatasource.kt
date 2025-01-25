package com.poulastaa.setup.domain.repository.set_artist

import androidx.paging.PagingData
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.core.domain.model.DtoArtist
import com.poulastaa.setup.domain.model.DtoPrevArtist
import kotlinx.coroutines.flow.Flow

interface RemoteSetArtistDatasource {
    fun suggestArtist(query: String): Flow<PagingData<DtoPrevArtist>>
    suspend fun storeArtist(list: List<ArtistId>): Result<List<DtoArtist>, DataError.Network>
}