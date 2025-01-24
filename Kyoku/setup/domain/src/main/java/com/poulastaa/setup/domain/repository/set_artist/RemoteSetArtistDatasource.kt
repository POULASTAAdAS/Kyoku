package com.poulastaa.setup.domain.repository.set_artist

import androidx.paging.PagingData
import com.poulastaa.setup.domain.model.DtoPrevArtist
import kotlinx.coroutines.flow.Flow

interface RemoteSetArtistDatasource {
    fun suggestArtist(query: String): Flow<PagingData<DtoPrevArtist>>
}