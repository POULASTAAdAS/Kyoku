package com.poulastaa.add.domain.repository

import androidx.paging.PagingData
import com.poulastaa.add.domain.model.DtoAddArtist
import com.poulastaa.add.domain.model.DtoAddArtistFilterType
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.EmptyResult
import com.poulastaa.core.domain.model.ArtistId
import kotlinx.coroutines.flow.Flow

interface AddArtistRepository {
    suspend fun searchArtist(
        query: String,
        filterType: DtoAddArtistFilterType,
    ): Flow<PagingData<DtoAddArtist>>

    suspend fun saveArtist(list: List<ArtistId>): EmptyResult<DataError.Network>
}