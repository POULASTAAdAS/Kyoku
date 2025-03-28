package com.poulastaa.explore.domain.repository

import androidx.paging.PagingData
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.core.domain.model.DtoPrevArtist
import com.poulastaa.explore.domain.model.DtoAllFromArtistItem
import kotlinx.coroutines.flow.Flow

interface AllFromArtistRepository {
    suspend fun getArtist(artistId: ArtistId): Result<DtoPrevArtist, DataError.Network>
    fun getSongs(artistId: ArtistId, query: String): Flow<PagingData<DtoAllFromArtistItem>>
    fun getAlbums(artistId: ArtistId, query: String): Flow<PagingData<DtoAllFromArtistItem>>
}