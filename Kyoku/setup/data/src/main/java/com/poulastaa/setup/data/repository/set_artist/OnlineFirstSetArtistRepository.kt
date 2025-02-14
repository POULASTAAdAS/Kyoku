package com.poulastaa.setup.data.repository.set_artist

import androidx.paging.PagingData
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.EmptyResult
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.asEmptyDataResult
import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.core.domain.repository.LocalSetArtistDatasource
import com.poulastaa.core.domain.model.DtoPrevArtist
import com.poulastaa.setup.domain.repository.set_artist.RemoteSetArtistDatasource
import com.poulastaa.setup.domain.repository.set_artist.SetArtistRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OnlineFirstSetArtistRepository @Inject constructor(
    private val remote: RemoteSetArtistDatasource,
    private val local: LocalSetArtistDatasource,
) : SetArtistRepository {
    override fun suggestArtist(query: String): Flow<PagingData<DtoPrevArtist>> =
        remote.suggestArtist(query)

    override suspend fun storeArtist(list: List<ArtistId>): EmptyResult<DataError.Network> {
        val result = remote.storeArtist(list)
        if (result is Result.Success) local.storeArtist(result.data)

        return result.asEmptyDataResult()
    }
}