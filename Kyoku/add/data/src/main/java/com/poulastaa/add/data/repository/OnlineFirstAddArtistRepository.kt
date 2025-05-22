package com.poulastaa.add.data.repository

import androidx.paging.PagingData
import androidx.paging.filter
import com.poulastaa.add.domain.model.DtoAddArtist
import com.poulastaa.add.domain.model.DtoAddArtistFilterType
import com.poulastaa.add.domain.repository.AddArtistRepository
import com.poulastaa.add.domain.repository.RemoteAddArtistDatasource
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.EmptyResult
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.asEmptyDataResult
import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.core.domain.repository.LocalAddArtistDatasource
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

internal class OnlineFirstAddArtistRepository @Inject constructor(
    private val local: LocalAddArtistDatasource,
    private val remote: RemoteAddArtistDatasource,
    private val scope: CoroutineScope,
) : AddArtistRepository {
    override suspend fun searchArtist(
        query: String,
        filterType: DtoAddArtistFilterType,
    ): Flow<PagingData<DtoAddArtist>> {
        val savedArtist = scope.async { local.loadSavedArtist() }
        val paging = scope.async { remote.searchArtist(query, filterType) }

        val savedArtistList = savedArtist.await()
        val pagingList = paging.await()

        return pagingList.map { list ->
            list.filter { it.id !in savedArtistList }
        }
    }

    override suspend fun saveArtist(
        list: List<ArtistId>,
    ): EmptyResult<DataError.Network> = remote.saveArtist(list).let {
        if (it is Result.Success) scope.launch { local.savedArtist(it.data) }
        it.asEmptyDataResult()
    }
}