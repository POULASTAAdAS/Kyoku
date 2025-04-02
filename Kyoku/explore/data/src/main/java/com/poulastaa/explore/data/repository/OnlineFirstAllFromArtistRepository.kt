package com.poulastaa.explore.data.repository

import androidx.paging.PagingData
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.core.domain.model.DtoPrevArtist
import com.poulastaa.core.domain.repository.LocalAllFromArtistDatasource
import com.poulastaa.explore.domain.model.DtoExploreItem
import com.poulastaa.explore.domain.repository.all_from_artist.AllFromArtistRepository
import com.poulastaa.explore.domain.repository.all_from_artist.RemoteAllFromArtistDatasource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class OnlineFirstAllFromArtistRepository @Inject constructor(
    private val remote: RemoteAllFromArtistDatasource,
    private val local: LocalAllFromArtistDatasource,
) : AllFromArtistRepository {
    override suspend fun getArtist(artistId: ArtistId): Result<DtoPrevArtist, DataError.Network> {
        val dbArtist = local.getArtist(artistId)

        return if (dbArtist != null) Result.Success(dbArtist)
        else remote.getArtist(artistId)
    }

    override fun getSongs(
        artistId: ArtistId,
        query: String,
    ): Flow<PagingData<DtoExploreItem>> = remote.getSongs(artistId, query)

    override fun getAlbums(
        artistId: ArtistId,
        query: String,
    ): Flow<PagingData<DtoExploreItem>> = remote.getAlbums(artistId, query)
}