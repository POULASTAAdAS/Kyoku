package com.poulastaa.add.domain.repository

import androidx.paging.PagingData
import com.poulastaa.add.domain.model.DtoAddArtist
import com.poulastaa.add.domain.model.DtoAddArtistFilterType
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.core.domain.model.DtoArtist
import kotlinx.coroutines.flow.Flow

interface RemoteAddArtistDatasource {
    fun searchArtist(
        query: String,
        filterType: DtoAddArtistFilterType,
    ): Flow<PagingData<DtoAddArtist>>

    suspend fun saveArtist(list: List<ArtistId>): Result<List<DtoArtist>, DataError.Network>
}