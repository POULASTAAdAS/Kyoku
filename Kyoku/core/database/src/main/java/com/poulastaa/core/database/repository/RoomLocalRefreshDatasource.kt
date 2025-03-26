package com.poulastaa.core.database.repository

import com.poulastaa.core.domain.model.DtoPrevAlbum
import com.poulastaa.core.domain.model.DtoPrevArtist
import com.poulastaa.core.domain.model.DtoPrevSong
import com.poulastaa.core.domain.model.DtoRelationSuggestedArtistSong
import com.poulastaa.core.domain.repository.LocalRefreshDatasource
import javax.inject.Inject

internal class RoomLocalRefreshDatasource @Inject constructor(): LocalRefreshDatasource {
    override suspend fun clearSuggestedData() {
        TODO("Not yet implemented")
    }

    override suspend fun updatePopularSongMix(list: List<DtoPrevSong>) {
        TODO("Not yet implemented")
    }

    override suspend fun updatePopularArtistMix(list: List<DtoPrevSong>) {
        TODO("Not yet implemented")
    }

    override suspend fun updateOldGem(list: List<DtoPrevSong>) {
        TODO("Not yet implemented")
    }

    override suspend fun updateSuggestedArtist(list: List<DtoPrevArtist>) {
        TODO("Not yet implemented")
    }

    override suspend fun updateSuggestedAlbum(list: List<DtoPrevAlbum>) {
        TODO("Not yet implemented")
    }

    override suspend fun storePrevSong(list: List<DtoPrevSong>) {
        TODO("Not yet implemented")
    }

    override suspend fun storeSuggestedArtist(list: List<DtoPrevArtist>) {
        TODO("Not yet implemented")
    }

    override suspend fun updateRelationSuggestedArtistSong(list: List<DtoRelationSuggestedArtistSong>) {
        TODO("Not yet implemented")
    }
}