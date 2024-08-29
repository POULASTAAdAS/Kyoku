package com.poulastaa.setup.data

import com.poulastaa.core.domain.model.Artist
import com.poulastaa.core.domain.repository.artist.ArtistRepository
import com.poulastaa.core.domain.repository.artist.LocalArtistDataSource
import com.poulastaa.core.domain.repository.artist.RemoteArtistDataSource
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.EmptyResult
import com.poulastaa.core.domain.utils.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import javax.inject.Inject

class OnlineFirstArtistRepository @Inject constructor(
    private val local: LocalArtistDataSource,
    private val remote: RemoteArtistDataSource,
    private val applicationScope: CoroutineScope,
) : ArtistRepository {
    override suspend fun getArtist(
        sentList: List<Long>,
    ): Result<List<Artist>, DataError.Network> = applicationScope.async {
        remote.getArtist(sentList)
    }.await()

    override suspend fun insertArtists(
        entrys: List<Artist>,
    ): EmptyResult<DataError.Network> = applicationScope.async {
        val response = remote.storeArtist(
            idList = entrys.map {
                it.id
            }
        )

        if (response is Result.Success) local.insertArtist(entrys)

        response
    }.await()
}