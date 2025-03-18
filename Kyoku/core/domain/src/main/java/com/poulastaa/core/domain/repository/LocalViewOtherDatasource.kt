package com.poulastaa.core.domain.repository

import com.poulastaa.core.domain.model.DtoDetailedPrevSong
import com.poulastaa.core.domain.model.DtoExploreType
import com.poulastaa.core.domain.model.DtoSong
import com.poulastaa.core.domain.model.DtoViewPayload
import com.poulastaa.core.domain.model.PlaylistId
import com.poulastaa.core.domain.model.SongId
import com.poulastaa.core.domain.model.ViewType

interface LocalViewOtherDatasource {
    suspend fun getSavedPrevViewSongIds(type: ViewType): List<SongId>

    suspend fun getViewTypeData(
        type: ViewType,
        otherId: Long? = null,
    ): DtoViewPayload<DtoDetailedPrevSong>?

    suspend fun saveViewTypeData(
        list: List<DtoSong>,
        playlistId: PlaylistId? = null,
    )

    suspend fun saveViewTypeData(
        type: DtoExploreType,
        list: List<DtoSong>,
    )
}