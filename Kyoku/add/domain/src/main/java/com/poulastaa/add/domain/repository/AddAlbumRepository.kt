package com.poulastaa.add.domain.repository

import androidx.paging.PagingData
import com.poulastaa.add.domain.model.DtoAddAlbum
import com.poulastaa.add.domain.model.DtoAddAlbumSearchFilterType
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.EmptyResult
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.model.AlbumId
import com.poulastaa.core.domain.model.DtoDetailedPrevSong
import kotlinx.coroutines.flow.Flow

interface AddAlbumRepository {
    suspend fun loadAlbum(
        query: String,
        filterType: DtoAddAlbumSearchFilterType,
    ): Flow<PagingData<DtoAddAlbum>>

    suspend fun getAlbum(albumId: AlbumId): Result<List<DtoDetailedPrevSong>, DataError.Network>

    suspend fun saveAlbums(list: List<AlbumId>): EmptyResult<DataError.Network>
}