package com.poulastaa.add.network.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.gson.Gson
import com.poulastaa.add.domain.model.DtoAddArtist
import com.poulastaa.add.domain.model.DtoAddArtistFilterType
import com.poulastaa.add.domain.repository.RemoteAddArtistDatasource
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.core.domain.model.DtoArtist
import kotlinx.coroutines.flow.Flow
import okhttp3.OkHttpClient
import javax.inject.Inject

internal class OkHttpAddArtistDatasource @Inject constructor(
    private val paging: AddArtistPagingSource,
    private val client: OkHttpClient,
    private val gson: Gson,
) : RemoteAddArtistDatasource {
    override fun searchArtist(
        query: String,
        filterType: DtoAddArtistFilterType,
    ): Flow<PagingData<DtoAddArtist>> = Pager(
        config = PagingConfig(
            pageSize = 10,
            initialLoadSize = 30,
        ),
        initialKey = 1,
        pagingSourceFactory = { paging.init(query, filterType) }
    ).flow

    override suspend fun saveArtist(list: List<ArtistId>): Result<List<DtoArtist>, DataError.Network> {
        TODO("Not yet implemented")
    }
}