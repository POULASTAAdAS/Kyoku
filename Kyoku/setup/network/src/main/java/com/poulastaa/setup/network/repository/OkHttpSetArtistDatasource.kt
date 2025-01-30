package com.poulastaa.setup.network.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.gson.Gson
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.map
import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.core.domain.model.DtoArtist
import com.poulastaa.core.domain.model.EndPoints
import com.poulastaa.core.network.ApiMethodType
import com.poulastaa.core.network.mapper.toDtoArtist
import com.poulastaa.core.network.model.UpsertArtistReq
import com.poulastaa.core.network.model.UpsertOperation
import com.poulastaa.core.network.model.UpsertReq
import com.poulastaa.core.network.req
import com.poulastaa.setup.domain.model.DtoPrevArtist
import com.poulastaa.setup.domain.repository.set_artist.RemoteSetArtistDatasource
import com.poulastaa.setup.network.model.SaveArtistRes
import com.poulastaa.setup.network.paging_source.SuggestArtistPagingSource
import kotlinx.coroutines.flow.Flow
import okhttp3.OkHttpClient
import javax.inject.Inject

class OkHttpSetArtistDatasource @Inject constructor(
    private val client: OkHttpClient,
    private val gson: Gson,
    private val artist: SuggestArtistPagingSource,
) : RemoteSetArtistDatasource {
    override fun suggestArtist(query: String): Flow<PagingData<DtoPrevArtist>> {
        artist.init(query)

        return Pager(
            config = PagingConfig(
                pageSize = 15,
                enablePlaceholders = false
            ),
            initialKey = 1,
            pagingSourceFactory = { artist }
        ).flow
    }

    override suspend fun storeArtist(list: List<ArtistId>): Result<List<DtoArtist>, DataError.Network> {
        val result = client.req<UpsertArtistReq, SaveArtistRes>(
            route = EndPoints.UPSERTArtist.route,
            method = ApiMethodType.POST,
            body = UpsertArtistReq(
                data = UpsertReq(
                    list = list,
                    operation = UpsertOperation.INSERT
                )
            ),
            gson = gson
        )

        return result.map { res -> res.list.map { it.toDtoArtist() } }
    }
}