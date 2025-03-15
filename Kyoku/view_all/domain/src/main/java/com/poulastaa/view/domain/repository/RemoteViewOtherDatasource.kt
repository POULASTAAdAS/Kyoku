package com.poulastaa.view.domain.repository

import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.model.DtoDetailedPrevSong
import com.poulastaa.core.domain.model.DtoSong
import com.poulastaa.core.domain.model.DtoViewPayload
import com.poulastaa.core.domain.model.SongId
import com.poulastaa.core.domain.model.ViewType

interface RemoteViewOtherDatasource {
    suspend fun getViewData(otherId: Long? = null): Result<DtoViewPayload<DtoSong>, DataError.Network>

    suspend fun getViewAlbum(
        type: ViewType,
        albumId: Long,
    ): Result<DtoViewPayload<DtoDetailedPrevSong>, DataError.Network>

    suspend fun getViewData(
        type: ViewType,
        savedSongIdList: List<SongId>,
    ): Result<DtoViewPayload<DtoSong>, DataError.Network>
}