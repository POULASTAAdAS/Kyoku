package com.poulastaa.setup.network

import com.google.gson.Gson
import com.poulastaa.core.data.network.post
import com.poulastaa.core.data.network.put
import com.poulastaa.core.domain.EndPoints
import com.poulastaa.core.domain.model.Artist
import com.poulastaa.core.domain.repository.artist.RemoteArtistDataSource
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.EmptyResult
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.domain.utils.asEmptyDataResult
import com.poulastaa.core.domain.utils.map
import com.poulastaa.setup.network.mappers.toArtist
import com.poulastaa.setup.network.model.req.GetArtistReq
import com.poulastaa.setup.network.model.req.StoreArtistReq
import com.poulastaa.setup.network.model.res.SuggestArtistDto
import okhttp3.OkHttpClient
import javax.inject.Inject

class ArtistRemoteDataSource @Inject constructor(
    private val client: OkHttpClient,
    private val gson: Gson,
) : RemoteArtistDataSource {
    override suspend fun getArtist(
        sentList: List<Long>,
    ): Result<List<Artist>, DataError.Network> = client.post<GetArtistReq, SuggestArtistDto>(
        route = EndPoints.SuggestArtist.route,
        body = GetArtistReq(sentList),
        gson = gson
    ).map {
        it.listOfArtist.map { entry ->
            entry.toArtist()
        }
    }

    override suspend fun storeArtist(
        idList: List<Long>,
    ): EmptyResult<DataError.Network> = client.put<StoreArtistReq, Boolean>(
        route = EndPoints.StoreArtist.route,
        body = StoreArtistReq(idList),
        gson = gson
    ).asEmptyDataResult()
}