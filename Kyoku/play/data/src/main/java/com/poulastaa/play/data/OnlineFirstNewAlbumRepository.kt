package com.poulastaa.play.data

import androidx.paging.PagingData
import com.poulastaa.core.domain.model.AlbumPagingType
import com.poulastaa.core.domain.model.PagingAlbumData
import com.poulastaa.core.domain.repository.new_album.LocalNewAlbumDataSource
import com.poulastaa.core.domain.repository.new_album.NewAlbumRepository
import com.poulastaa.core.domain.repository.new_album.RemoteNewAlbumDataSource
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.EmptyResult
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.domain.utils.asEmptyDataResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OnlineFirstNewAlbumRepository @Inject constructor(
    private val local: LocalNewAlbumDataSource,
    private val remote: RemoteNewAlbumDataSource,
    private val applicationScope: CoroutineScope
) : NewAlbumRepository {
    override fun getPagingAlbum(
        query: String,
        type: AlbumPagingType
    ): Flow<PagingData<PagingAlbumData>> = remote.getPagingAlbum(query, type)

    override suspend fun saveAlbums(list: List<Long>): EmptyResult<DataError.Network> {
        val notSavedAlbumIdList = local.getNotSavedAlbumIdList(list)
        val result = remote.saveAlbums(notSavedAlbumIdList)

        if (result is Result.Success) applicationScope.async { local.saveAlbums(result.data) }
            .await()

        return result.asEmptyDataResult()
    }
}