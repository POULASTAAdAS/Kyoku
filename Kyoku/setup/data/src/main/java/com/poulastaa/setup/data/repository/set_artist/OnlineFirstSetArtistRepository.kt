package com.poulastaa.setup.data.repository.set_artist

import androidx.paging.PagingData
import com.poulastaa.setup.domain.model.DtoPrevArtist
import com.poulastaa.setup.domain.repository.set_artist.RemoteSetArtistDatasource
import com.poulastaa.setup.domain.repository.set_artist.SetArtistRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OnlineFirstSetArtistRepository @Inject constructor(
    private val remote: RemoteSetArtistDatasource,
//    private val local: LocalSetArtistDatasource
) : SetArtistRepository {
    override fun suggestArtist(query: String): Flow<PagingData<DtoPrevArtist>> =
        remote.suggestArtist(query)
}