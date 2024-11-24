package com.poulastaa.play.data

import androidx.paging.PagingData
import com.poulastaa.core.domain.model.Artist
import com.poulastaa.core.domain.model.ArtistPagingType
import com.poulastaa.core.domain.repository.new_artist.LocalNewArtistDataSource
import com.poulastaa.core.domain.repository.new_artist.NewArtistRepository
import com.poulastaa.core.domain.repository.new_artist.RemoteNewArtistDataSource
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.EmptyResult
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.domain.utils.asEmptyDataResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OnlineFirstNewArtistRepository @Inject constructor(
    private val local: LocalNewArtistDataSource,
    private val remote: RemoteNewArtistDataSource,
    private val applicationScope: CoroutineScope,
) : NewArtistRepository {
    override fun getPagingArtist(
        query: String,
        type: ArtistPagingType,
    ): Flow<PagingData<Artist>> = remote.getPagingArtist(
        query = query,
        type = type,
    )

    override suspend fun saveArtist(list: List<Long>): EmptyResult<DataError.Network> {
        val result = remote.saveArtist(list)
        if (result is Result.Success) applicationScope.async { local.saveArtist(result.data) }
            .await()



        return result.asEmptyDataResult()
    }
}