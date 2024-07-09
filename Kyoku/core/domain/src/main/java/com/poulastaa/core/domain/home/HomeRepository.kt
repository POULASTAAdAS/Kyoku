package com.poulastaa.core.domain.home

import com.poulastaa.core.domain.model.DayType
import com.poulastaa.core.domain.model.HomeData
import com.poulastaa.core.domain.model.NewHome
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.EmptyResult

interface HomeRepository {
    suspend fun storeNewHomeResponse(dayType: DayType): EmptyResult<DataError.Network>

    suspend fun isNewUser(): Boolean

    suspend fun loadHomeData(): HomeData
}