package com.poulastaa.view.domain.repository

import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.model.DtoDetailedPrevSong
import com.poulastaa.core.domain.model.DtoViewPayload
import com.poulastaa.core.domain.model.ViewType

interface ViewOtherRepository {
    suspend fun getViewData(
        type: ViewType,
        otherId: Long,
    ): Result<DtoViewPayload<DtoDetailedPrevSong>, DataError>
}