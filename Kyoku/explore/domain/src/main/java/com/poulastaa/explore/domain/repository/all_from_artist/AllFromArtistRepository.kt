package com.poulastaa.explore.domain.repository.all_from_artist

import androidx.paging.PagingData
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.core.domain.model.DtoPrevArtist
import com.poulastaa.explore.domain.model.DtoExploreItem
import kotlinx.coroutines.flow.Flow

interface AllFromArtistRepository {
    suspend fun getArtist(artistId: ArtistId): Result<DtoPrevArtist, DataError.Network>
    fun getSongs(artistId: ArtistId, query: String): Flow<PagingData<DtoExploreItem>>
    fun getAlbums(artistId: ArtistId, query: String): Flow<PagingData<DtoExploreItem>>
}